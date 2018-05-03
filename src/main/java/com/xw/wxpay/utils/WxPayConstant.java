package com.xw.wxpay.utils;

public class WxPayConstant {

    /******************************需要客户配置项******************************/
    // 基础回调
    public static final String RETURN_URL = "";
	// 公众号AppId
    public static final String APP_ID = "";
	// 公众号AppSecret
    public static final String APP_SECRET = "";
	// 微信支付商户号
    public static final String MCH_ID = "";
	// 微信支付API秘钥
    public static final String KEY = "";
    
    /******************************需要客户配置项******************************/

    
    // 微信返回成功的标志
    public static final String RETURN_SUCCESS = "SUCCESS";
    // 授权后回调地址
    public static final String AFTER_AUTHORIZE_RETURN_URL = RETURN_URL + "/wx/pay";
    // 微信授权的地址
    public static final String GET_AUTHORIZE = "https://open.weixin.qq.com/connect/oauth2/authorize?";
	// 通过code获取授权access_token的URL
    public static final String GET_AUTHTOKEN_URL = " https://api.weixin.qq.com/sns/oauth2/access_token?";
    // 支付成功后回调地址
    public static final String PAY_SUCCESS_RETURN_URL = RETURN_URL + "/wx/notifyUrl";
	// 微信统一下单url
    public static final String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	// 微信交易类型:公众号支付
    public static final String TRADE_TYPE_JSAPI = "JSAPI";
    // 收到支付成功的回调后返回给微信的成功的信息
    public static final String PAY_SUCCESS_RETURN_MSG = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
    // 收到支付成功的回调后返回给微信的签名失败的信息
    public static final String SIGN_FAIL_RETURN_MSG = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[check sign fail]]></return_msg></xml>";
    
}