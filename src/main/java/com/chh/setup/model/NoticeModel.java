package com.chh.setup.model;

import com.chh.setup.dto.res.process.PageSuperDto;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author chh
 * @date 2020/2/24 15:07
 */
@Data
@Entity
@Table(name = "notice")
public class NoticeModel implements PageSuperDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer notifierId;
    private Integer receiverId;
    private Integer type;  // 1(commentId)，2(articleId), 3(commentId)
    private Integer parentId;
    private Integer state;
    private Date createTime;

    @Transient
    private String action;
    
    @Transient
    private UserModel notifier;

    @Transient
    private ArticleModel article;
    
    @Transient
    private CommentModel comment;
}
