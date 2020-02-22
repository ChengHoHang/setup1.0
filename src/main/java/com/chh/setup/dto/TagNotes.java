package com.chh.setup.dto;

import lombok.Data;

import java.util.List;

/**
 * @author chh
 * @date 2020/2/19 19:53
 */
@Data
public class TagNotes {

    private String typeName;
    private String categoryName;
    private List<TagDto> tags;

    public TagNotes(String typeName, String categoryName, List<TagDto> tags) {
        this.typeName = typeName;
        this.categoryName = categoryName;
        this.tags = tags;
    }
}
