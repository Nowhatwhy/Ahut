package org.nowhatwhy.ahut.exception;

import lombok.Getter;
import org.nowhatwhy.ahut.constant.ErrorCode;

@Getter
public class BusinessException extends RuntimeException {
    private final Integer code;
    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }
    public BusinessException(String message){
        super(message);
        this.code = ErrorCode.DEFAULT_ERROR;
    }
}
