package com.chh.setup.enums.tag;

import com.chh.setup.dto.TagDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum EntertainmentTag {
    STAR("401", "明星"),
    TV("402", "电视剧"),
    FILM("403", "电影");

    private String id;
    private String remarks;
    private static Map<String, String> map = new HashMap<>();
    private static List<TagDto> list = new ArrayList<>();

    static {
        for (EntertainmentTag value : EntertainmentTag.values()) {
            map.put(value.getId(), value.getRemarks());
            list.add(new TagDto(value.getId(), value.getRemarks()));
        }
    }
    
    EntertainmentTag(String id, String remarks) {
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
