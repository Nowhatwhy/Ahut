package org.nowhatwhy.ahut.exception;

import lombok.extern.slf4j.Slf4j;
import org.nowhatwhy.ahut.constant.ErrorCode;
import org.nowhatwhy.ahut.entity.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler{
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Result<?>> handle(BusinessException e) {
        return ResponseEntity
                .ok()
                .body(Result.fail(e.getCode(), e.getMessage()));
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Result<?>> handle(HttpMessageNotReadableException e) {
        return ResponseEntity
                .badRequest()
                .body(Result.fail(ErrorCode.PARAM_ERROR, "参数类型错误"));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Result<?>> handle(MethodArgumentTypeMismatchException e) {
        return ResponseEntity
                .badRequest()
                .body(Result.fail(ErrorCode.PARAM_ERROR, "参数类型不匹配"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result<?>> handle(Exception e) {
        log.error("系统异常", e);
        return ResponseEntity
                .status(500)
                .body(Result.fail(ErrorCode.SYSTEM_ERROR, "系统异常"));
    }
}
