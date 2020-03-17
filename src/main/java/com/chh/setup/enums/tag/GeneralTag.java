package com.chh.setup.enums.tag;

import java.util.ArrayList;
import java.util.List;

public enum GeneralTag {
    SOCIAL("社会"),
    COMMERCE("商业"),
    MILITARY("军事"),
    CULTURE("文化"),
    CHARACTER("人物"),
    HYGIENE("卫生"),
    TECHNOLOGY("科技");

    private String remark;
    private static List<String> list = new ArrayList<>();

    static {
        for (GeneralTag value : GeneralTag.values()) {
             list.add(value.getRemark());
        }
    }

    GeneralTag(String remark) {
        this.remark = remark;
    }
    
    public String getRemark() {
        return remark;
    }

    public static List<String> getRemarks() {
        return list;
    }
    
}
