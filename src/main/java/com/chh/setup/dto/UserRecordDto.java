package com.chh.setup.dto;

import lombok.Data;

/**
 * 之后补充评论、点赞记录信息
 * @author chh
 * @date 2020/1/20 15:41
 */
@Data
public class UserRecordDto {
    Integer userId;
    PagesDto data;
    String recordType;
}
