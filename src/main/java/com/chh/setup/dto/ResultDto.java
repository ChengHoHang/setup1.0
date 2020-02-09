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
        return ResultDto.errorOf(baseExceptionInterface.getErrorCode(), baseExceptionInterface.getErrorMsg());
    }
    
    public static ResultDto errorOf(CustomizeException ex) {
        return ResultDto.errorOf(ex.getErrorCode(), ex.getErrorMsg());
    }

    public static <T> ResultDto okOf(T t) {
        ResultDto resultDto = new ResultDto();
        resultDto.setCode(200);
        resultDto.setMessage("请求成功");
        resultDto.setRsData(t);
        return resultDto;
    }
}
