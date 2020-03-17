package com.chh.setup.service;

import com.chh.setup.advice.exception.CustomizeErrorCode;
import com.chh.setup.advice.exception.CustomizeException;
import com.chh.setup.dto.req.CommentParam;
import com.chh.setup.enums.NoticeTypeEnum;
import com.chh.setup.model.ArticleModel;
import com.chh.setup.model.CommentModel;
import com.chh.setup.model.UserModel;
import com.chh.setup.repository.ArticleRepository;
import com.chh.setup.repository.CommentRepository;
import com.chh.setup.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author chh
 * @date 2020/2/4 19:13
 */
@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private NoticeService noticeService;

    /**
     * 先搞新建，编辑功能有时间再搞
     * @param commentParam
     * @param currentUser
     */
    @Transactional
    public void createCommentAndNotice(CommentParam commentParam, UserModel currentUser) {
        CommentModel commentModel = new CommentModel();
        commentModel.setCreateTime(new Date());
        commentModel.setUpdateTime(new Date());
        BeanUtils.copyProperties(commentParam, commentModel, "createTime", "updateTime");
        ArticleModel article = articleRepository.findById(commentParam.getArticleId()).orElse(null);
        if (article == null) {
            throw new CustomizeException(CustomizeErrorCode.ARTICLE_NOT_FOUND);
        }
        CommentModel comment = commentRepository.save(commentModel);
        articleRepository.incCommentCount(commentModel.getArticleId(), 1);
        
        if (!currentUser.getId().equals(commentParam.getAuthorId())) {  //通知作者回复
            noticeService.createNotice(commentParam.getCommentatorId(), commentParam.getAuthorId(),
                    NoticeTypeEnum.REPLAY_ARTICLE.getType(), comment.getId());
        }
    }

    /**
     * 获取文章id下的所有评论
     * @param id 文章id
     * @return 对应文章id下的所有评论
     */
    public List<CommentModel> getCommentsByArticleId(Integer id) {
        Sort sort = Sort.by(Sort.Direction.ASC, "createTime");
        List<CommentModel> comments = commentRepository.findAllByArticleId(id, sort);
        comments.forEach(comment -> comment.setCommentator(userRepository.findById(comment.getCommentatorId()).get()));
        return comments;
    }
    
}
