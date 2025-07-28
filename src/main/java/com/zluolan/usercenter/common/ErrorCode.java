package com.zluolan.usercenter.common;

import lombok.Getter;

/**
 * 错误码枚举（与BaseResponse配合使用）
 * - 2xx 成功类
 * - 4xx 客户端错误类
 * - 5xx 服务端错误类
 * 其中后三位可根据模块划分或错误类型细分
 */
@Getter
public enum ErrorCode {
    SUCCESS(0, "ok", ""),
    PARAMS_ERROR(4001001, "请求参数错误", "具体参数错误描述"),
    NOT_FOUND_ERROR(4041001, "数据不存在", ""),
    SYSTEM_ERROR(5001001, "系统内部异常", ""),
    NOT_LOGIN_ERROR(4011001, "未登录", ""),
    NO_AUTH_ERROR(4031001, "无权限", "");

    // getter方法...
    private final int code;
    private final String message;
    private final String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

}
