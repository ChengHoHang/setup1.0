package com.chh.setup.dto;

import com.chh.setup.entity.UserEntity;
import lombok.Data;

/**
 * 之后补充评论、点赞记录信息
 * @author chh
 * @date 2020/1/20 15:41
 */
@Data
public class UserRecordDto<T> {
    UserEntity user;
    PagesDto<T> data;
}
