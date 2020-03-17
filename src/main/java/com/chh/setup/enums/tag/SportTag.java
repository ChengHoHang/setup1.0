package com.chh.setup.enums.tag;

import java.util.ArrayList;
import java.util.List;

public enum SportTag {
    BASKETBALL("篮球"),
    FOOTBALL("足球"),
    BADMINTON("羽毛球"),
    ESPORTS("电子竞技");

    private String remark;
    private static List<String> list = new ArrayList<>();;

    static {
        for (SportTag value : SportTag.values()) {
            list.add(value.getRemark());
        }
    }
    
    SportTag(String remark) {
        this.remark = remark;
    }
    
    public String getRemark() {
        return remark;
    }

    public static List<String> getRemarks() {
        return list;
    }
    
}
