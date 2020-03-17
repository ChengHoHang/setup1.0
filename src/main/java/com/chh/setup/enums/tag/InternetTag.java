package com.chh.setup.enums.tag;

import java.util.ArrayList;
import java.util.List;

public enum InternetTag {
    JAVA("java"),
    SPRINGBOOT("springboot"),
    MYSQL("mysql"),
    REDIS("redis"),
    ES("elasticsearch"),
    PYTHON("python");

    private String remark;
    private static List<String> list = new ArrayList<>();

    static {
        for (InternetTag value : InternetTag.values()) {
            list.add(value.getRemark());
        }
    }

    InternetTag(String remark) {
        this.remark = remark;
    }
    
    public String getRemark() {
        return remark;
    }

    public static List<String> getRemarks() {
        return list;
    }
    
}
