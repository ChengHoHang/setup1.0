package com.chh.setup.enums;

import com.chh.setup.exception.CustomizeErrorCode;
import com.chh.setup.exception.CustomizeException;

public enum NoticeTypeEnum {
    REPLAY_ARTICLE(1, "回复了文章"),
    LIKE_ARTICLE(2, "点赞了文章"),
    LIKE_COMMENT(3, "点赞了评论");
    private Integer type;
    private String action;

    public Integer getType() {
        return type;
    }

    public String getAction() {
        return action;
    }

    NoticeTypeEnum(Integer type, String action) {
        this.type = type;
        this.action = action;
    }

    public static String getAction(int type) {
        for (NoticeTypeEnum value : NoticeTypeEnum.values()) {
            if (value.getType() == type) {
                return value.getAction();
            }
        }
        throw new CustomizeException(CustomizeErrorCode.PARAM_ERROR);
    }
}
