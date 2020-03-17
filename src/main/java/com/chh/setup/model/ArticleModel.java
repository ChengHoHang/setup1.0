package com.chh.setup.model;

import com.chh.setup.dto.res.process.PageSuperDto;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @author chh
 * @date 2020/1/10 20:33
 */
@Data
@Entity
@Table(name = "article")
public class ArticleModel implements PageSuperDto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String description;
    private Integer categoryId;
    private String tag;
    private Integer authorId;
    private Integer commentCount = 0;
    private Integer viewCount = 0;
    private Integer likeCount = 0;
    private Date createTime;
    private Date updateTime;

    @Transient
    private String type;
    
    @Transient
    private String[] tags;
    
    @Transient
    private UserModel author;

    @Transient
    private List<CommentModel> comments;
    
    @Transient
    private Integer favorState = 0;   //当前session用户是否喜欢该文章；若未登录默认为0

    @Transient
    private List<Object[]> relatedArticle;
    
    public ArticleModel() { }

    public ArticleModel(ArticleModel articleModel, UserModel userModel) {
        BeanUtils.copyProperties(articleModel, this);
        this.setAuthor(userModel);
    }
}
