package com.chh.setup.enums.tag;

import com.chh.setup.dto.TagDto;

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
    private String remarks;
    private static Map<String, String> map = new HashMap<>();
    private static List<TagDto> list = new ArrayList<>();

    static {
        for (SportTag value : SportTag.values()) {
            map.put(value.getId(), value.getRemarks());
            list.add(new TagDto(value.getId(), value.getRemarks()));
        }
    }
    
    SportTag(String id, String remarks) {
        this.id = id;
        this.remarks = remarks;
    }

    public String getId() {
        return id;
    }

    public String getRemarks() {
        return remarks;
    }

    public static List<TagDto> getDtos() {
        return list;
    }
    
    public static Map<String, String> getMap() {
        return map;
    }
}
