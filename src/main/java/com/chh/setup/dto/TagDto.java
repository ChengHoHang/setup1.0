package com.chh.setup.dto;

import lombok.Data;

/**
 * @author chh
 * @date 2020/2/16 23:09
 */
@Data
public class TagDto {

    private String id;
    private String remarks;

    public TagDto(String id, String remarks) {
        this.id = id;
        this.remarks = remarks;
    }
}