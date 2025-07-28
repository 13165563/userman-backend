package com.zluolan.usercenter.common;

/**
 * 返回统一封装类
 * @author zluolan
 */
public class ResultUtils {
    /**
     * 成功响应（带默认消息）
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, data, "ok", "");
    }

    /**
     * 失败响应（使用预定义错误码）
     */
    public static <T> BaseResponse<T> error(ErrorCode errorCode) {
        if (errorCode == null) {
            throw new IllegalArgumentException("errorCode 不能为空");
        }
        return new BaseResponse<>(errorCode);
    }

    /**
     * 失败响应（自定义状态码和信息）
     */
    public static <T> BaseResponse<T> error(int code, String message, String description) {
        return new BaseResponse<>(code, message, description);
    }

    /**
     * 失败响应（基于错误码并自定义消息和描述）
     */
    public static <T> BaseResponse<T> error(ErrorCode errorCode, String message, String description) {
        if (errorCode == null) {
            throw new IllegalArgumentException("errorCode 不能为空");
        }
        return new BaseResponse<>(errorCode.getCode(), message, description);
    }

    /**
     * 失败响应（基于错误码并自定义描述）
     */
    public static <T> BaseResponse<T> error(ErrorCode errorCode, String description) {
        if (errorCode == null) {
            throw new IllegalArgumentException("errorCode 不能为空");
        }
        return new BaseResponse<>(errorCode.getCode(), errorCode.getMessage(), description);
    }
}
