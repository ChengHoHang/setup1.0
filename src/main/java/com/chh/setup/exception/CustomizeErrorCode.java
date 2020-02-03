package com.chh.setup.exception;

/**
 * 实现BaseExceptionInterface的原因：将user、article等等服务层的异常分类，CustomizeErrorCode是通用的异常类
 * 而user抑或是article对应的异常类都实现BaseExceptionInterface接口，在使用上直接调用该接口实现类即可，使用方便
 * user、article逻辑上只有自己对应的异常类，后面有需要的话再分类
 * @author chh
 * @date 2020/2/2 18:52
 */
public enum CustomizeErrorCode implements BaseExceptionInterface {
    SUCCESS(200, "成功"),
    ARTICLE_NOT_FOUND(2005, "文章不存在"),
    USER_LOG_OUT(3001, "用户未登录"),
    EDIT_PERMISSION_DENY(3002, "用户无权限修改其他文章");

    private Integer errorCode;

    private String errorMsg;

    CustomizeErrorCode(Integer errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    @Override
    public Integer getErrorCode() {
        return errorCode;
    }

    @Override
    public String getErrorMsg() {
        return errorMsg;
    }
}
