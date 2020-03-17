package com.chh.setup.advice;

import com.chh.setup.advice.exception.CustomizeErrorCode;
import com.chh.setup.advice.exception.CustomizeException;
import com.chh.setup.advice.exception.JumpExcetion;
import com.chh.setup.dto.res.ResultDto;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * @author chh
 * @date 2020/2/2 17:48
 */
@RestControllerAdvice
public class CustomizeExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Object handle(HttpServletResponse response, Exception ex) throws IOException {
        ex.printStackTrace();
        if (ex instanceof CustomizeException) {
            return ResultDto.errorOf((CustomizeException) ex);
        } else if (ex instanceof MissingServletRequestParameterException) {
            return ResultDto.errorOf(CustomizeErrorCode.PARAM_ERROR);
        } else if (ex instanceof JumpExcetion) {
            response.sendRedirect("/error.html?"
                    + ((JumpExcetion) ex).getErrorCode() + "&"
                    + URLEncoder.encode(((JumpExcetion) ex).getErrorMsg(), "UTF-8"));
            return null;
        } else if (ex instanceof MethodArgumentTypeMismatchException) {
            response.sendRedirect("/error.html");
            return null;
        } else {
            return ResultDto.errorOf(CustomizeErrorCode.SYS_ERROR);
        }
    }

}
