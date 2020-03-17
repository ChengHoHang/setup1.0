package com.chh.setup.advice.exception;

/**
 * @author chh
 * @date 2020/2/2 18:52
 */
public enum CustomizeErrorCode implements IBaseException {

    LOGIN_FAIL(1001, "您输入的账号不存在或者密码错误，请重试！"),
    REGISTER_PARAM_INVALID(1002, "非法注册参数"),
    REGISTER_ACCOUNT_EXIST(1003, "该账号已存在，请重新设置账号！"),
    
    ARTICLE_NOT_FOUND(2005, "文章不存在，要不换一篇看看？"),
    CATEGORY_NOT_EXISIT(2006, "文章类型不存在"),
    TAG_NOT_EXIST(2007, "不存在此标签"),
    NOTICE_NOT_FOUND(2008, "消息不翼而飞了..."),
    
    USER_LOG_OUT(3001, "用户未登录，请登陆后重试~"),
    EDIT_PERMISSION_DENY(3002, "用户无权限修改其他文章"),
    USER_NOT_EXIST(3003, "用户不存在"),
    OPERATE_PERMISSION_DENY(3004, "用户无权限操作"),

    SYS_ERROR(5001, "服务器冒烟了~~~请稍后再访问~~~"),
    PARAM_ERROR(5002, "参数错误");
    
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
