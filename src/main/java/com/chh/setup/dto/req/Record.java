package com.chh.setup.dto.req;

import lombok.Data;

/**
 * @author chh
 * @date 2020/2/9 12:50
 */
@Data
public class Record {

    private Integer commentId;
    private Integer commentatorId;
    private Integer state;   // 0或1   1表示点赞  0表示取消点赞
}