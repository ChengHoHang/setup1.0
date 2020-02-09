package com.chh.setup.dto;

import com.chh.setup.entity.UserEntity;
import lombok.Data;

/**
 * @author chh
 * @date 2020/2/5 0:23
 */
@Data
public class CommentDto {
    
    private Integer id;
    private String content;
    private Integer likeCount;
    private String gmtCreated;
    private UserEntity commentator;
    private Integer favorState = 0;   //当前session用户是否喜欢该评论；若未登录默认为0
}
