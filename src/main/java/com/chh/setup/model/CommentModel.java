package com.chh.setup.model;

import com.chh.setup.dto.res.process.PageSuperDto;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author chh
 * @date 2020/2/4 18:58
 */
@Data
@Entity
@Table(name = "comment")
public class CommentModel implements PageSuperDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer articleId;
    private Integer commentatorId;
    private String content;
    private Integer likeCount = 0;
    private Date createTime;
    private Date updateTime;

    @Transient
    private UserModel commentator;
    
    @Transient
    private String articleTitle;

    @Transient
    private Integer favorState = 0;   //当前session用户是否喜欢该评论；若未登录默认为0
}
