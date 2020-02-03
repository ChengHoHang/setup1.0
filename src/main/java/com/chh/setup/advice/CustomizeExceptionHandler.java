package com.chh.setup.advice;

import com.chh.setup.dto.ResultDto;
import com.chh.setup.exception.CustomizeException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author chh
 * @date 2020/2/2 17:48
 */
@RestControllerAdvice
public class CustomizeExceptionHandler {

    @ExceptionHandler(CustomizeException.class)
    public Object handle(CustomizeException ex) {
        return ResultDto.errorOf(ex);
    }
    
}
