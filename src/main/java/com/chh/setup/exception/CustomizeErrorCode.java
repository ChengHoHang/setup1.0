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

    REGISTER_ACCOUNT_ABNORMAL(1001, "您输入的账号不存在或者密码错误，请重试！"),
    REGISTER_PARAM_ERROR(1002, "账号或者密码不能为空！"),
    REGISTER_ACCOUNT_EXIST(1003, "该账号已存在，请重新设置账号！"),
    
    BLANK_TITLE(2001, "标题不能为空"),
    BLANK_DESCRIPTION(2002, "新闻内容不能为空"),
    BLANK_TYPE(2003, "类型不能为空"),
    BLANK_TAG(2004, "标签不能为空"),
    ARTICLE_NOT_FOUND(2005, "文章不存在，要不换一篇看看？"),
    TYPE_NOT_EXIST(2006, "文章类型不存在"),

    BLANK_CONTENT(2051, "评论内容不能为空"),
    
    USER_LOG_OUT(3001, "用户未登录，请登陆后重试~"),
    EDIT_PERMISSION_DENY(3002, "用户无权限修改其他文章"),
    USER_NOT_EXIST(3003, "用户不存在");

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
