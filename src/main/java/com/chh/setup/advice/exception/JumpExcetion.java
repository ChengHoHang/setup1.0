package com.chh.setup.advice.exception;

/**
 * 页面跳转外加异常类
 * @author chh
 * @date 2020/3/14 20:50
 */
public class JumpExcetion extends RuntimeException{

    public Integer errorCode;

    public String errorMsg;

    public JumpExcetion(IBaseException exceptionInterface) {
        this.errorCode = exceptionInterface.getErrorCode();
        this.errorMsg = exceptionInterface.getErrorMsg();
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
