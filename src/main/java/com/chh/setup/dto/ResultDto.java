package com.chh.setup.dto;

import com.chh.setup.exception.BaseExceptionInterface;
import com.chh.setup.exception.CustomizeException;
import lombok.Data;

/**
 * 后端与前端交互的数据传输对象
 * @param <T>
 */
@Data
public class ResultDto<T> {

    private Integer code;
    private String message;
    private T rsData;

    public static ResultDto errorOf(Integer code, String message) {
        ResultDto resultDto = new ResultDto();
        resultDto.setCode(code);
        resultDto.setMessage(message);
        return resultDto;
    }

    public static ResultDto errorOf(BaseExceptionInterface baseExceptionInterface) {
        ResultDto resultDto = new ResultDto();
        resultDto.setCode(baseExceptionInterface.getErrorCode());
        resultDto.setMessage(baseExceptionInterface.getErrorMsg());
        return resultDto;
    }
    
    public static ResultDto errorOf(CustomizeException ex) {
        ResultDto resultDto = new ResultDto();
        resultDto.setCode(ex.getErrorCode());
        resultDto.setMessage(ex.getErrorMsg());
        return resultDto;
    }

    public static <T> ResultDto okOf(T t) {
        ResultDto resultDto = new ResultDto();
        resultDto.setCode(200);
        resultDto.setMessage("请求成功");
        resultDto.setRsData(t);
        return resultDto;
    }
}
