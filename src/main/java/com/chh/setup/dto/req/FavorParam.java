package com.chh.setup.dto.req;

import lombok.Data;

import java.util.List;

/**
 * @author chh
 * @date 2020/2/6 23:44
 */
@Data
public class FavorParam {
    
    private Integer articleId;
    private Integer articleAuthor;
    private List<Record> records;
    private Integer favorState;   // 若无改变则接收null。 1表示点赞， 0表示取消点赞
    private Integer currentUserId;
}