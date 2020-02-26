package com.chh.setup.enums;

/**
 * @author chh
 * @date 2020/2/25 12:41
 */
public enum  UserRecordEnum {
    ARTICLE("article"),
    COMMENT("comment"),
    FAVOR("favor");
    
    private String action;

    public String getAction() {
        return action;
    }

    UserRecordEnum(String action) {
        this.action = action;
    }
}
