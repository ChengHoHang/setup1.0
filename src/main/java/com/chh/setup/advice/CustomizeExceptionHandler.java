package com.chh.setup.advice;

import com.chh.setup.dto.ResultDto;
import com.chh.setup.exception.CustomizeErrorCode;
import com.chh.setup.exception.CustomizeException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author chh
 * @date 2020/2/2 17:48
 */
@RestControllerAdvice
public class CustomizeExceptionHandler {

        @ExceptionHandler(Exception.class)
        public Object handle(Exception ex) {
            if (ex instanceof CustomizeException) {
                ex.printStackTrace();
                return ResultDto.errorOf((CustomizeException) ex);
            } else {
                System.out.println(ex.getMessage());
                ex.printStackTrace();
                return ResultDto.errorOf(CustomizeErrorCode.SYS_ERROR);
            } 
        }

    }
