package com.zluolan.usercenter.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用响应封装类
 * @param <T> 响应数据类型
 */
@Data
public class BaseResponse<T> implements Serializable {


    private int code;
    private T data;
    private String message;
    private String description;
    // 显式添加无参构造函数
    public BaseResponse() {
        this.code = 0;
        this.data = null;
        this.message = "";
        this.description = "";
    }

    public BaseResponse(int code, T data, String message, String description) {
        this.code = code;
        this.data = data;
        this.message = (message != null) ? message : "";
        this.description = (description != null) ? description : "";
    }
    public BaseResponse(int code, T data,String message) {
        this(code, data, message, "");
    }
    public BaseResponse(int code,String message, String description) {
        this(code, null, message, description);
    }
    public BaseResponse(int code, T data) {
        this(code, data, "", "");
    }
    public BaseResponse(ErrorCode errorCode) {
        this(
                errorCode.getCode(),
                null,
                (errorCode.getMessage() != null) ? errorCode.getMessage() : "",
                (errorCode.getDescription() != null) ? errorCode.getDescription() : ""
        );
    }

    public BaseResponse(ErrorCode errorCode, String description) {
        this(
                errorCode.getCode(),
                null,
                (errorCode.getMessage() != null) ? errorCode.getMessage() : "",
                (description != null) ? description : ""
        );
    }

}
