package com.chh.setup.enums.tag;

import com.chh.setup.dto.TagDto;

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
    private String remarks;
    private static Map<String, String> map = new HashMap<>();
    private static List<TagDto> list = new ArrayList<>();

    static {
        for (InternetTag value : InternetTag.values()) {
            map.put(value.getId(), value.getRemarks());
            list.add(new TagDto(value.getId(), value.getRemarks()));
        }
    }

    InternetTag(String id, String remarks) {
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
