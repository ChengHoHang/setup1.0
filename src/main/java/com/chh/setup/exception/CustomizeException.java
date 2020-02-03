package com.chh.setup.exception;

/**
 * @author chh
 * @date 2020/2/2 17:54
 */
public class CustomizeException extends RuntimeException {
    
    public Integer errorCode;

    public String errorMsg;

    public CustomizeException(Integer errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public CustomizeException(BaseExceptionInterface exceptionInterface) {
        this.errorCode = exceptionInterface.getErrorCode();
        this.errorMsg = exceptionInterface.getErrorMsg();
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
