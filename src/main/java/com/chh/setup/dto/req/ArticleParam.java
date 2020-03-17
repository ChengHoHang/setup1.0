package com.chh.setup.dto.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author chh
 * @date 2020/1/10 20:44
 */
@Data
public class ArticleParam {
    
    private Integer articleId;

    @NotNull(message = "类型不能为空")
    private Integer categoryId;

    @NotBlank(message = "标题不能为空")
    private String title;

    @NotBlank(message = "正文不能为空")
    private String description;

    @NotBlank(message = "标签不能为空")
    private String tag;

    @NotNull(message = "用户未登录")
    private Integer authorId;
}
