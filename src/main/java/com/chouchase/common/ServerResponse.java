package com.chouchase.common;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServerResponse<T> {
    private final int code;
    private String msg;
    private T data;

    private ServerResponse(int code) {
        this.code = code;
    }

    private ServerResponse(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private ServerResponse(int code, T date) {
        this.code = code;
        this.data = date;
    }

    private ServerResponse(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> ServerResponse<T> createSuccessResponse() {
        return new ServerResponse<>(ResponseCode.SUCCESS);
    }

    public static <T> ServerResponse<T> createSuccessResponseByMsg(String msg) {
        return new ServerResponse<>(ResponseCode.SUCCESS, msg);
    }

    public static <T> ServerResponse<T> createSuccessResponseByData(T data) {
        return new ServerResponse<>(ResponseCode.SUCCESS, data);
    }

    public static <T> ServerResponse<T> createSuccessResponseByMsgAndData(String msg, T data) {
        return new ServerResponse<>(ResponseCode.SUCCESS, msg, data);
    }


    public static <T> ServerResponse<T> createFailResponse() {
        return new ServerResponse<>(ResponseCode.FAIL);
    }

    public static <T> ServerResponse<T> createFailResponseByMsg(String msg) {
        return new ServerResponse<>(ResponseCode.FAIL, msg);
    }

    public static <T> ServerResponse<T> createFailResponseByData(T data) {
        return new ServerResponse<>(ResponseCode.FAIL, data);
    }

    public static <T> ServerResponse<T> createFailResponseByMsgAndData(String msg, T data) {
        return new ServerResponse<>(ResponseCode.FAIL, msg, data);
    }

    @JsonIgnore
    public boolean isSuccess() {
        return this.code == ResponseCode.SUCCESS;
    }

    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }

    public T getData() {
        return data;
    }

}
