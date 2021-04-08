package com.chouchase.common;

public enum OrderStatus {
    CANCELED(0,"已取消"),
    UNPAID(1,"未付款"),
    PAID(2,"已付款"),
    SENT(3,"已发货"),
    SUCCESS(4,"交易成功"),
    CLOSED(5,"交易关闭");


    private int code;
    private String msg;
    private OrderStatus(int code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }



    public String getMsg() {
        return msg;
    }
    public static String codeOf(Integer code){
        for(OrderStatus orderStatus : values()){
            if(orderStatus.code == code){
                return orderStatus.msg;
            }
        }
        return null;
    }

}
