package com.xw.wxpay.business;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.xw.wxpay.bean.AuthToken;
import com.xw.wxpay.bean.WxPaySendData;
import com.xw.wxpay.global.WxPayConstant;
import com.xw.wxpay.utils.WxPayUtils;

public class WxPayBusiness {

	private WxPayBusiness(){}
	public static class WxPayInstance {
		private static WxPayBusiness wxPayBusiness = new WxPayBusiness();
		public static WxPayBusiness getWxPayBusiness() {
			return wxPayBusiness;
		}
	}
	public static WxPayBusiness self() {
		return WxPayInstance.getWxPayBusiness();
	}
	
//	public static Logger LOG = LoggerFactory.getLogger(WxPayBusiness.class);
	
	/**
	 * 授权
	 * @param response
	 * @param ordId
	 * @throws Exception
	 */
	public void authorize(HttpServletResponse response, Long ordId) throws Exception {
		String backUri = WxPayConstant.AFTER_AUTHORIZE_RETURN_URL + "?ordId=" + ordId;
		String state = UUID.randomUUID().toString().trim().replaceAll("-", "");

		// scope这里用scope=snsapi_base不弹出授权页面直接授权 只获取统一支付接口的openid
		String url = WxPayConstant.GET_AUTHORIZE + "appid=" + WxPayConstant.APP_ID + "&redirect_uri=" + backUri
				+ "&display=mobile" + "&response_type=code&scope=snsapi_base&state=" + state + "#wechat_redirect";
		
		try {
			response.sendRedirect(url);
		} catch (Exception e) {
//			LOG.error("微信支付获取授权失败！", e);
			throw new Exception(e);
		}
	}
	
	/**
	 * 统一下单
	 * @param map
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> unifiedOrderBusiness(Map<String, String> map, HttpServletRequest request) throws Exception {
		
		Map<String, String> result = Maps.newHashMap();
		
		// 获取统一下单后的参数
		Map<String, String> resultMap = unifiedOrder(map, request);

		if (null == resultMap) {
//			LOG.error("微信统一下单失败，resultMap空，订单编号:" + map.get("OutTradeNo"));
			throw new Exception("微信统一下单失败，resultMap空，订单编号:" + map.get("OutTradeNo"));
		}

		String returnCode = resultMap.get("return_code");// 通信标识
		String resultCode = resultMap.get("result_code");// 交易标识

//		LOG.info("微信支付统一下单end，resultMap：" + resultMap);

		// 只有当returnCode与resultCode均返回success，才代表微信支付统一下单成功
		if (WxPayConstant.RETURN_SUCCESS.equals(resultCode) && WxPayConstant.RETURN_SUCCESS.equals(returnCode)) {
//			LOG.info("微信支付统一下单成功！");

			String appId = resultMap.get("appid");// 微信公众号AppId
			String timeStamp = WxPayUtils.getTimeStamp();// 当前时间戳
			String prepayId = "prepay_id=" + resultMap.get("prepay_id");// 统一下单返回的预支付id
			String nonceStr = WxPayUtils.getRandomStr(32);// 不长于32位的随机字符串
			SortedMap<String, String> signMap = Maps.newTreeMap();// 自然升序map
			signMap.put("appId", appId);
			signMap.put("package", prepayId);
			signMap.put("timeStamp", timeStamp);
			signMap.put("nonceStr", nonceStr);
			signMap.put("signType", "MD5");
			
			result.put("appId", appId);                                  
			result.put("timeStamp", timeStamp);                          
			result.put("nonceStr", nonceStr);                            
			result.put("prepayId", prepayId);                            
			result.put("signType", "MD5");                               
			result.put("paySign", WxPayUtils.createSign(signMap));// 获取签名
		} else {
//			LOG.error("微信统一下单失败,订单编号:" + map.get("OutTradeNo") + ",失败原因:" + resultMap.get("return_msg"));
			throw new Exception("微信统一下单失败,订单编号:" + map.get("OutTradeNo") + ",失败原因:" + resultMap.get("return_msg"));
		}
		return result;
	}
	
	public SortedMap<String,String> paySuccess(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String xmlResult = "";
		try {
			xmlResult = WxPayUtils.getXmlRequest(request);
		} catch (Exception e) {
//			LOG.info("微信支付回调获取xmlResult失败，xmlResult", e);
			throw new Exception(e);
		}
//		LOG.info("微信支付回调，xmlResult" + xmlResult);
		
		SortedMap<String,String> resultMap = null;
		try {
			resultMap = WxPayUtils.xml2Map(xmlResult);
		} catch (Exception e) {
//			LOG.error("微信支付回调结果参数解析错误！回调结果：" + xmlResult, e);
			throw new Exception(e);
		}
		
		String result = "";// 回调后返回给微信的结果，不返回微信会回调多次
		if (resultMap != null && resultMap.size() > 0) {
			String signReceive = (String) resultMap.get("sign");
			resultMap.remove("sign");// 去掉sign，获取签名再对比。
			String checkSign = WxPayUtils.getSign(resultMap);

			// 签名校验成功
			if (checkSign != null && signReceive != null && checkSign.equals(signReceive.trim())) {

				String returnCode = resultMap.get("return_code");
				String resultCode = resultMap.get("result_code");
				// 支付成功
				if (WxPayConstant.RETURN_SUCCESS.equals(returnCode) && WxPayConstant.RETURN_SUCCESS.equals(resultCode)) {

//					LOG.info("微信支付成功，out_trade_no(erpId_ordId)：" + resultMap.get("out_trade_no"));
					result = WxPayConstant.PAY_SUCCESS_RETURN_MSG;
					
					resultMap.put("success", "success");
				} else {
					String returnMsg = resultMap.get("return_msg");
//					LOG.error("微信支付失败，return_msg：" + returnMsg);
					
					result = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA["
							+ returnMsg + "]]></return_msg></xml>";
				}
			} else {
				// 签名校验失败
//				LOG.error("微信支付签名校验失败！");
				result = WxPayConstant.SIGN_FAIL_RETURN_MSG;
			}
		}

		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/xml");
		response.getWriter().write(result);
		response.getWriter().flush();
		response.getWriter().close();
		return resultMap;
	}
	
	private AuthToken getAccessToken(HttpServletRequest request) throws Exception {
		// 用户同意授权，获得的code
		String code = request.getParameter("code");

		AuthToken authToken = null;

		// 通过code获取网页授权access_token
		try {
			authToken = getTokenByAuthCode(code);
		} catch (Exception e) {
//			LOG.error("获取access_token异常！");
			throw new Exception("获取access_token异常！");
		}
		if (null == authToken) {
//			LOG.error("获取access_token异常，authToken空！");
			throw new Exception("获取access_token异常，authToken空！");
		}
//		LOG.info("微信支付获取获取网页授权end，authToken：" + authToken);
		return authToken;
	}
	
	private AuthToken getTokenByAuthCode(String code) throws Exception {
		AuthToken authToken;
		StringBuilder json = new StringBuilder();

		URL url = new URL(WxPayConstant.GET_AUTHTOKEN_URL + "appid=" + WxPayConstant.APP_ID + "&secret="
				+ WxPayConstant.APP_SECRET + "&code=" + code + "&grant_type=authorization_code");
		URLConnection uc = url.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
		String inputLine;
		while ((inputLine = in.readLine()) != null) {
			json.append(inputLine);
		}
		in.close();
		authToken = JSONObject.parseObject(json.toString(), AuthToken.class);

		return authToken;
	}
	
	/**
	 * 统一下单处理
	 * @param map 
	 * @return
	 * @throws Exception 
	 */
	private Map<String, String> unifiedOrder(Map<String, String> map, HttpServletRequest request) throws Exception {

		AuthToken authToken = getAccessToken(request);
		
		Map<String, String> resultMap = null;
		try {
			WxPaySendData paySendData = new WxPaySendData();

			// 构建微信支付请求参数集合
			// 必填
			paySendData.setAppId(WxPayConstant.APP_ID);// 公众账号ID
			paySendData.setMchId(WxPayConstant.MCH_ID);// 商户号
			paySendData.setNonceStr(UUID.randomUUID().toString().replaceAll("-", ""));// 随机字符串
			paySendData.setBody(new String((map.get("body")).getBytes("ISO-8859-1"), "UTF-8"));// 商品描述，需要u8编码
			paySendData.setOutTradeNo(map.get("OutTradeNo"));// 商户订单号
			paySendData.setTotalFee(1);// 标价金额，订单总金额，单位为分
			paySendData.setSpBillCreateIp(request.getRemoteAddr());// 终端IP
			paySendData.setNotifyUrl(WxPayConstant.PAY_SUCCESS_RETURN_URL);// 微信支付成功后回调通知地址
			paySendData.setTradeType(WxPayConstant.TRADE_TYPE_JSAPI);// 交易类型
			paySendData.setOpenId(authToken.getOpenid());// 用户标识，jsapi支付时必填
			// 选填
			paySendData.setDeviceInfo("WEB");// 设备号

			// 将参数拼成map，生成签名
			SortedMap<String, String> params = buildParamMap(paySendData);
			paySendData.setSign(WxPayUtils.createSign(params));// 签名

			// 将请求参数对象转换成xml
			String reqXml = WxPayUtils.sendDataToXml(paySendData);
//			LOG.info("微信支付统一下单begin，reqXml：" + reqXml);

			// 发送请求
			byte[] xmlData = reqXml.getBytes();
			URL url = new URL(WxPayConstant.UNIFIED_ORDER_URL);
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setDoOutput(true);
			urlConnection.setDoInput(true);
			urlConnection.setUseCaches(false);
			urlConnection.setRequestProperty("Content_Type", "text/xml");
			urlConnection.setRequestProperty("Content-length", String.valueOf(xmlData.length));
			DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
			outputStream.write(xmlData);
			outputStream.flush();
			outputStream.close();
			resultMap = WxPayUtils.parseXml(urlConnection.getInputStream());
		} catch (Exception e) {
//			LOG.error("微信统一下单失败，订单编号:" + map.get("OutTradeNo"));
			throw new Exception(e);
		}
		return resultMap;
	}
	
	/**
	 * 构建统一下单参数map 用于生成签名
	 * 
	 * @param data
	 * @return SortedMap<String,Object>
	 */
	private SortedMap<String, String> buildParamMap(WxPaySendData data) {
		SortedMap<String, String> paramters = new TreeMap<String, String>();
		if (null != data) {
			if (StringUtils.isNotEmpty(data.getAppId())) {
				paramters.put("appid", data.getAppId());
			}
			if (StringUtils.isNotEmpty(data.getMchId())) {
				paramters.put("mch_id", data.getMchId());
			}
			if (StringUtils.isNotEmpty(data.getDeviceInfo())) {
				paramters.put("device_info", data.getDeviceInfo());
			}
			if (StringUtils.isNotEmpty(data.getNonceStr())) {
				paramters.put("nonce_str", data.getNonceStr());
			}
			if (StringUtils.isNotEmpty(data.getBody())) {
				paramters.put("body", data.getBody());
			}
			if (StringUtils.isNotEmpty(data.getOutTradeNo())) {
				paramters.put("out_trade_no", data.getOutTradeNo());
			}
			if (data.getTotalFee() > 0) {
				paramters.put("total_fee", data.getTotalFee()+"");
			}
			if (StringUtils.isNotEmpty(data.getSpBillCreateIp())) {
				paramters.put("spbill_create_ip", data.getSpBillCreateIp());
			}
			if (StringUtils.isNotEmpty(data.getTradeType())) {
				paramters.put("trade_type", data.getTradeType());
			}
			if (StringUtils.isNotEmpty(data.getNotifyUrl())) {
				paramters.put("notify_url", data.getNotifyUrl());
			}
			if (StringUtils.isNotEmpty(data.getOpenId())) {
				paramters.put("openid", data.getOpenId());
			}
		}
		return paramters;
	}
}
