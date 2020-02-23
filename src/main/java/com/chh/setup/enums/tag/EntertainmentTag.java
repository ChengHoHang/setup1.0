package com.chh.setup.enums.tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum EntertainmentTag {
    STAR("401", "明星"),
    TV("402", "电视剧"),
    FILM("403", "电影");

    private String id;
    private String remark;
    private static Map<String, String> map = new HashMap<>();
    private static List<String> list;

    static {
        for (EntertainmentTag value : EntertainmentTag.values()) {
            map.put(value.getId(), value.getRemark());
        }
        list = new ArrayList<>(map.values());
    }
    
    EntertainmentTag(String id, String remark) {
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
