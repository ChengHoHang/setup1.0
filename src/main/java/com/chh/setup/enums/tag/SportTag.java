package com.chh.setup.enums.tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum SportTag {
    BASKETBALL("501", "篮球"),
    FOOTBALL("502", "足球"),
    BADMINTON("503", "羽毛球"),
    ESPORTS("504", "电子竞技");

    private String id;
    private String remark;
    private static Map<String, String> map = new HashMap<>();
    private static List<String> list;

    static {
        for (SportTag value : SportTag.values()) {
            map.put(value.getId(), value.getRemark());
        }
        list = new ArrayList<>(map.values());
    }
    
    SportTag(String id, String remark) {
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
