package com.zluolan.usercenter.exception;

import com.zluolan.usercenter.common.BaseResponse;
import com.zluolan.usercenter.common.ErrorCode;
import com.zluolan.usercenter.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler( BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(BusinessException e) {
        log.error("RuntimeException: {}", e.getMessage(), e);
        return ResultUtils.error(e.getCode(), e.getMessage(), e.getDescription());
    }
    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeException(RuntimeException e) {
        log.error("RuntimeException: {}", e.getMessage(), e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, e.getMessage(), "");
    }
    @ExceptionHandler(Exception.class)
    public BaseResponse<?> exceptionHandler(Exception e) {
        log.error("Exception: {}", e.getMessage(), e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "系统内部异常", "");
    }
}
