package com.chh.setup.enums.tag;

import com.chh.setup.dto.TagDto;

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
    private String remarks;
    private static Map<String, String> map = new HashMap<>();
    private static List<TagDto> list = new ArrayList<>();

    static {
        for (GeneralTag value : GeneralTag.values()) {
            map.put(value.getId(), value.getRemarks());
            list.add(new TagDto(value.getId(), value.getRemarks()));
        }
    }

    GeneralTag(String id, String remarks) {
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
