package com.xw.wxpay.utils;

import java.io.Serializable;

public class AuthToken implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
    private String access_token;// access_token
    private String expires_in;// 有效期
    private String refresh_token;// 刷新access_token
    private String openid;// 用户OPENID
    private String scope;// 授权方式Scope
    private String errcode;// 错误码
    private String errmsg;// 错误消息
    
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public String getExpires_in() {
		return expires_in;
	}
	public void setExpires_in(String expires_in) {
		this.expires_in = expires_in;
	}
	public String getRefresh_token() {
		return refresh_token;
	}
	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public String getErrcode() {
		return errcode;
	}
	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}
	public String getErrmsg() {
		return errmsg;
	}
	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
	@Override
	public String toString() {
		return "AuthToken [access_token=" + access_token + ", expires_in=" + expires_in + ", refresh_token="
				+ refresh_token + ", openid=" + openid + ", scope=" + scope + ", errcode=" + errcode + ", errmsg="
				+ errmsg + "]";
	}
}