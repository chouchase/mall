package com.chouchase.common;

public enum PaymentType {
    ONLINE_PAY(0,"在线支付");
    private int code;
    private String msg;
    PaymentType(int code,String msg){
        this.code = code;
        this.msg = msg;
    }
    public static String codeOf(Integer code){
        for(PaymentType paymentType : values()){
            if(paymentType.code == code){
                return paymentType.msg;
            }
        }
        return null;
    }
    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
