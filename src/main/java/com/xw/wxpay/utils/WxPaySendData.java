package com.xw.wxpay.utils;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class WxPaySendData {

    @XStreamAlias("appid")
    private String appId;// 公众账号ID

    @XStreamAlias("mch_id")
    private String mchId;// 商户号 必须

    @XStreamAlias("device_info")
    private String deviceInfo;// 设备号

    @XStreamAlias("nonce_str")
    private String nonceStr;// 随机字符串 必须

    @XStreamAlias("sign")
    private String sign;// 签名 必须

    @XStreamAlias("body")
    private String body;// 商品描述 必须

    @XStreamAlias("detail")
    private String detail;// 商品详情

    @XStreamAlias("attach")
    private String attach;// 附加数据

    @XStreamAlias("out_trade_no")
    private String outTradeNo;// 商户订单号 必须

    @XStreamAlias("fee_type")
    private String feeType;// 货币类型

    @XStreamAlias("total_fee")
    private int totalFee;// 交易金额（分） 必须[JSAPI，NATIVE，APP]

    @XStreamAlias("trade_type")
    private String tradeType;// 交易类型 [必须]

    @XStreamAlias("notify_url")
    private String notifyUrl;// 通知地址 [必须]

    @XStreamAlias("spbill_create_ip")
    private String spBillCreateIp;// 终端Ip [必须]

    @XStreamAlias("time_start")
    private String timeStart;// 订单生成时间yyyyMMddHHmmss

    @XStreamAlias("time_expire")
    private String timeExpire;// 订单失效时间yyyyMMddHHmmss 间隔>5min

    @XStreamAlias("openid")
    private String openId;// 用户标识 tradeType=JSAPI时必须

    @XStreamAlias("goods_tag")
    private String goodsTag;// 商品标记

    @XStreamAlias("product_id")
    private String productId;// 商品ID tradeType=NATIVE时必须

    @XStreamAlias("limit_pay")
    private String limitPay;// 指定支付方式


    // 以下属性为申请退款参数
    @XStreamAlias("transaction_id")
    private String transactionId;// 微信订单号 [商户订单号二选一]

    @XStreamAlias("out_refund_no")
    private String outRefundNo;// 商户退款单号 [必须]

    @XStreamAlias("refund_fee")
    private Integer refundFee;// 退款金额 [必须]

    @XStreamAlias("refund_fee_type")
    private String refundFeeType;// 货币种类

    @XStreamAlias("op_user_id")
    private String opUserId;// 操作员账号:默认为商户号 [必须]

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getMchId() {
		return mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}

	public String getDeviceInfo() {
		return deviceInfo;
	}

	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
	}

	public String getNonceStr() {
		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getFeeType() {
		return feeType;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}

	public int getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(int totalFee) {
		this.totalFee = totalFee;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getSpBillCreateIp() {
		return spBillCreateIp;
	}

	public void setSpBillCreateIp(String spBillCreateIp) {
		this.spBillCreateIp = spBillCreateIp;
	}

	public String getTimeStart() {
		return timeStart;
	}

	public void setTimeStart(String timeStart) {
		this.timeStart = timeStart;
	}

	public String getTimeExpire() {
		return timeExpire;
	}

	public void setTimeExpire(String timeExpire) {
		this.timeExpire = timeExpire;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getGoodsTag() {
		return goodsTag;
	}

	public void setGoodsTag(String goodsTag) {
		this.goodsTag = goodsTag;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getLimitPay() {
		return limitPay;
	}

	public void setLimitPay(String limitPay) {
		this.limitPay = limitPay;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getOutRefundNo() {
		return outRefundNo;
	}

	public void setOutRefundNo(String outRefundNo) {
		this.outRefundNo = outRefundNo;
	}

	public Integer getRefundFee() {
		return refundFee;
	}

	public void setRefundFee(Integer refundFee) {
		this.refundFee = refundFee;
	}

	public String getRefundFeeType() {
		return refundFeeType;
	}

	public void setRefundFeeType(String refundFeeType) {
		this.refundFeeType = refundFeeType;
	}

	public String getOpUserId() {
		return opUserId;
	}

	public void setOpUserId(String opUserId) {
		this.opUserId = opUserId;
	}
    
}