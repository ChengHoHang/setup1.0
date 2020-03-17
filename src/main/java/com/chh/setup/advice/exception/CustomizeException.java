package com.chh.setup.advice.exception;

/**
 * @author chh
 * @date 2020/2/2 17:54
 */
public class CustomizeException extends RuntimeException {
    
    public Integer errorCode;

    public String errorMsg;

    public CustomizeException(IBaseException exceptionInterface) {
        this.errorCode = exceptionInterface.getErrorCode();
        this.errorMsg = exceptionInterface.getErrorMsg();
    }

    public CustomizeException(IBaseException exceptionInterface, String processErrorMsg) {
        this.errorCode = exceptionInterface.getErrorCode();
        this.errorMsg = processErrorMsg;
    }

    public Integer getErrorCode() {
        return errorCode;
    }
    
    public String getErrorMsg() {
        return errorMsg;
    }
    
}
