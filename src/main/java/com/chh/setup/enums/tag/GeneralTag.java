package com.chh.setup.enums.tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum GeneralTag {
    SOCIAL("101", "社会"),
    COMMERCE("102","商业"),
    MILITARY("103","军事"),
    CULTURE("104", "文化"),
    CHARACTER("105","人物"),
    HYGIENE("106","卫生"),
    TECHNOLOGY("107","科技");

    private String id;
    private String remark;
    private static Map<String, String> map = new HashMap<>();
    private static List<String> list;

    static {
        for (GeneralTag value : GeneralTag.values()) {
            map.put(value.getId(), value.getRemark());
        }
        list = new ArrayList<>(map.values());
    }

    GeneralTag(String id, String remark) {
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
