package com.chh.setup.model.key;

import lombok.Data;

import java.io.Serializable;

/**
 * @author chh
 * @date 2020/2/8 20:28
 */
@Data
public class ArticleFavorKey implements Serializable {

    private Integer articleId;
    private Integer userId;
}
