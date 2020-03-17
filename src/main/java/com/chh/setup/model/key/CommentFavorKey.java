package com.chh.setup.model.key;

import lombok.Data;

import java.io.Serializable;

/**
 * @author chh
 * @date 2020/2/8 20:28
 */
@Data
public class CommentFavorKey implements Serializable {

    private Integer commentId;
    private Integer userId;
}
