package org.nowhatwhy.ahut.expection;

import lombok.extern.slf4j.Slf4j;
import org.nowhatwhy.ahut.enitity.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler{
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Result<?>> handle(BusinessException e) {
        return ResponseEntity
                .badRequest()
                .body(Result.fail(e.getCode(), e.getMessage()));
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result<?>> handle(Exception e) {
        log.error(e.toString());
        return ResponseEntity
                .status(500)
                .body(Result.fail(500, "系统异常"));
    }
}
