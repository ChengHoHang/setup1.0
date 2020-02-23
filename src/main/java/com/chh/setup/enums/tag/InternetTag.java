package com.chh.setup.enums.tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum InternetTag {
    JAVA("301", "java"),
    SPRINGBOOT("302", "springboot"),
    MYSQL("303", "mysql"),
    REDIS("304", "redis"),
    ES("305", "elasticsearch"),
    PYTHON("306", "python");

    private String id;
    private String remark;
    private static Map<String, String> map = new HashMap<>();
    private static List<String> list;

    static {
        for (InternetTag value : InternetTag.values()) {
            map.put(value.getId(), value.getRemark());
        }
        list = new ArrayList<>(map.values());
    }

    InternetTag(String id, String remark) {
        this.id = id;
        this.remark = remark;
    }

    public String getId() {
        return id;
    }

    public String getRemark() {
        return remark;
    }

    public static List<String> getRemarks() {
        return list;
    }
    
    public static Map<String, String> getMap() {
        return map;
    }
}
