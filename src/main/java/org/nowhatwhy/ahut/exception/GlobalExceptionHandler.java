package org.nowhatwhy.ahut.exception;

import lombok.extern.slf4j.Slf4j;
import org.nowhatwhy.ahut.constant.ErrorCode;
import org.nowhatwhy.ahut.entity.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler{
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Result<?>> handle(BusinessException e) {
        return ResponseEntity
                .ok()
                .body(Result.fail(e.getCode(), e.getMessage()));
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result<?>> handle(Exception e) {
        log.error(e.toString());
        return ResponseEntity
                .status(500)
                .body(Result.fail(ErrorCode.SYSTEM_ERROR, "系统异常"));
    }
}
