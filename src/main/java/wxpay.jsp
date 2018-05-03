<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="content-type" content="text/html;charset=utf-8"/>
    <title>微信安全支付</title>
    <script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
    <script type="text/javascript">
        //调用微信JS api 支付
        function jsApiCall() {  
            WeixinJSBridge.invoke(  
                            'getBrandWCPayRequest',  
                            {  
                            	"appId" : "${appId}",     //公众号名称，由商户传入      
                  	           "timeStamp": "${timeStamp}",         //时间戳，自1970年以来的秒数      
                  	           "nonceStr" : "${nonceStr}", //随机串      
                  	           "package" : "${prepayId}",     //统一订单号  
                  	           "signType" : "MD5",         //微信签名方式：      
                  	           "paySign" : "${paySign}" //支付签名   
                            },  
                            function(res) {  
                                if (res.err_msg == "get_brand_wcpay_request:ok") {  
                                    alert("success？");
                                } else {//这里支付失败和支付取消统一处理  
                                    alert("failed？"); 
                                }  
                            });  
        }

        function callpay()
        {
        	
            if (typeof WeixinJSBridge == "undefined"){
                if( document.addEventListener ){
                    document.addEventListener('WeixinJSBridgeReady', jsApiCall, false);
                }else if (document.attachEvent){
                    document.attachEvent('WeixinJSBridgeReady', jsApiCall); 
                    document.attachEvent('onWeixinJSBridgeReady', jsApiCall);
                }
            }else{
                jsApiCall();
            }
        }
    </script>
</head>
<body>
    </br></br></br></br>
    <div align="center">
        <button style="width:100%; height:100%; background-color:#FE6714; border:0px #FE6714 solid; cursor: pointer;  color:white;  font-size:100px;" type="button" onclick="javascript:callpay();return false;" >点击立即获得100万</button>
    </div>
</body>
</html>