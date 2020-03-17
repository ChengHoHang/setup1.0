package com.chh.setup.enums.tag;

import java.util.ArrayList;
import java.util.List;

public enum EntertainmentTag {
    STAR("明星"),
    TV("电视剧"),
    FILM("电影");

    private String remark;
    private static List<String> list = new ArrayList<>();;

    static {
        for (EntertainmentTag value : EntertainmentTag.values()) {
            list.add(value.getRemark());
        }
    }
    
    EntertainmentTag(String remark) {
        this.remark = remark;
    }
    
    public String getRemark() {
        return remark;
    }
    
    public static List<String> getRemarks() {
        return list;
    }

}
