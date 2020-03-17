package com.chh.setup.dto.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author chh
 * @date 2020/2/4 18:49
 */
@Data
public class CommentParam {

    @NotNull(message = "文章id不能为空")
    private Integer articleId;

    @NotNull(message = "评论人不能为空")
    private Integer commentatorId;

    @NotBlank(message = "评论内容不能为空")
    private String content;

    @NotNull(message = "文章作者不能为空")
    private Integer authorId;
}
