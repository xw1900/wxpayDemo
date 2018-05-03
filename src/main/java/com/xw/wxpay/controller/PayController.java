package com.xw.wxpay.controller;

import java.util.Map;
import java.util.SortedMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.common.collect.Maps;
import com.xw.wxpay.utils.WxPay;

/**
 * 微信支付
 */
@Controller
@RequestMapping(value = "/wx")
public class PayController {

//	public static final Logger LOG = LoggerFactory.getLogger(PayController.class);

//	@Autowired
//	private LinePlanOrdService linePlanOrdService;
	
	@RequestMapping(value = ("/pay/{ordId}"), method = RequestMethod.GET)
	public String userAuth(HttpServletResponse response, @PathVariable Long ordId) throws Exception {
//		LOG.info("微信支付begin，订单id：" + ordId);
		
		// 校验订单
//		LinePlanOrd linePlanOrd = linePlanOrdService.searchById(Global.getId(), ordId); // 订单信息
//		if (null == linePlanOrd) {
//			LOG.error("订单异常！");
//			throw new BusinessException(400002);
//		}

		WxPay.authorize(response, ordId);
		return null;
	}
	
	@RequestMapping(value = ("/pay"))
	public String pay(HttpServletRequest request, Model model) throws Exception{
		
		Long ordId = Long.parseLong(request.getParameter("ordId"));
		
//		LOG.info("微信支付获取授权成功后回调begin，订单id：" + ordId);
		
//		LinePlanOrd linePlanOrd = linePlanOrdService.searchById(Global.getId(), ordId); // 订单信息
		
		Long id = 100000L;
		
		Map<String, String> map = Maps.newHashMap();
		map.put("body", "商品id:" + id);
		map.put("OutTradeNo", id + "");

		// 获取统一下单后的参数
		Map<String, String> result = WxPay.unifiedOrderBusiness(map, request);

		model.addAttribute("appId", result.get("appId"));
		model.addAttribute("timeStamp", result.get("timeStamp"));
		model.addAttribute("nonceStr", result.get("nonceStr"));
		model.addAttribute("prepayId", result.get("prepayId"));
		model.addAttribute("signType", result.get("signType"));
		model.addAttribute("paySign", result.get("paySign"));

		// 将支付需要参数返回至页面，测试用wxpay.jsp
		return "/wxpay";
	}
	
	/**
	 * 微信异步回调
	 * @throws Exception 
	 */
	@RequestMapping("/notifyUrl")
	public String weixinReceive(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		LOG.info("微信支付回调begin。");
		
		SortedMap<String, String> map = WxPay.paySuccess(request, response);
		if ("success".equals(map.get("success"))) {
			handlePayBusiness(map);
		} else {
//			LOG.error("微信支付失败。");
		}
		
		return null;
	}
	
	private void handlePayBusiness(SortedMap<String,String> resultMap) {
		
//		LOG.info("微信支付成功回调业务begin，" + resultMap);
		
	}
}
