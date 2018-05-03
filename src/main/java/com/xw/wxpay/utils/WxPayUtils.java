package com.xw.wxpay.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.google.common.collect.Maps;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;

public class WxPayUtils {

//	public static final Logger LOG = LoggerFactory.getLogger(WxPayUtils.class);

	//扩展xstream,使其支持name带有"_"的节点
	public static XStream xStream = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
	
	/**
	 * 获取微信签名
	 * 
	 * @param map 请求参数集合
	 * @return 微信请求签名串
	 */
	public static String createSign(SortedMap<String, String> packageParams) {
		StringBuffer sb = new StringBuffer();
		Set<Entry<String, String>> es = packageParams.entrySet();
		Iterator<Entry<String, String>> it = es.iterator();
		while (it.hasNext()) {
			Entry<String, String> entry = it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		sb.append("key=" + WxPayConstant.KEY);

		String sign = MD5Util.MD5Encode(sb.toString(), "UTF-8").toUpperCase();
		return sign;
	}
	
	/**
	 * 获取回调后参数的签名
	 * @param paramMap
	 * @param key
	 * @return
	 */
	public static String getSign(SortedMap<String, String> paramMap) {
		List list = new ArrayList(paramMap.keySet());
		Object[] ary = list.toArray();
		Arrays.sort(ary);
		list = Arrays.asList(ary);
		String str = "";
		for (int i = 0; i < list.size(); i++) {
			str += list.get(i) + "=" + paramMap.get(list.get(i) + "") + "&";
		}
		str += "key=" + WxPayConstant.KEY;
		str = MD5Util.MD5Encode(str, "UTF-8").toUpperCase();

		return str;
	}

	/**
	 * xml转map集合
	 * @param xml xml参数
	 * @return map集合
	 * @throws Exception 
	 */
	public static SortedMap<String, String> xml2Map(String xml) throws Exception {
		SortedMap<String, String> map = Maps.newTreeMap();
		Document doc = null;
		
		doc = DocumentHelper.parseText(xml);
		if (doc == null) {
			return map;
		}
		Element root = doc.getRootElement();
		for (Iterator iterator = root.elementIterator(); iterator.hasNext();) {
			Element e = (Element) iterator.next();
			map.put(e.getName(), e.getText());
		}
		return map;
	}

	/**
	 * 处理xml请求信息
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	public static String getXmlRequest(HttpServletRequest request) throws Exception {
		BufferedReader bis = null;
		String result = "";
		try {
			bis = new BufferedReader(new InputStreamReader(request.getInputStream()));
			String line = null;
			while ((line = bis.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	
	/**
	 * 解析微信服务器发来的请求
	 * 
	 * @param inputStream
	 * @return 微信返回的参数集合
	 * @throws Exception 
	 */
	public static SortedMap<String, String> parseXml(InputStream inputStream) throws Exception {
		SortedMap<String, String> map = Maps.newTreeMap();

		// 获取request输入流
		SAXReader reader = new SAXReader();
		Document document = reader.read(inputStream);
		// 得到xml根元素
		Element root = document.getRootElement();
		// 得到根元素所有节点
		List<Element> elementList = root.elements();
		// 遍历所有子节点
		for (Element element : elementList) {
			map.put(element.getName(), element.getText());
		}
		// 释放资源
		inputStream.close();
		return map;
	}

	/**
	 * 请求参数转换成xml
	 * 
	 * @param data
	 * @return xml字符串
	 */
	public static String sendDataToXml(WxPaySendData data) {
		xStream.autodetectAnnotations(true);
		xStream.alias("xml", WxPaySendData.class);
		return xStream.toXML(data);
	}

	/**
	 * 获取当前时间戳
	 */
	public static String getTimeStamp() {
		return String.valueOf(System.currentTimeMillis() / 1000);
	}

	/**
	 * 获取指定长度的随机字符串
	 * 
	 * @param length 指定长度
	 * @return 随机字符串
	 */
	public static String getRandomStr(int length) {
		String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}
}