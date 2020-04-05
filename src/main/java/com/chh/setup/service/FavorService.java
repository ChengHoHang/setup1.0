package com.chh.setup.service;

import com.chh.setup.advice.exception.CustomizeErrorCode;
import com.chh.setup.advice.exception.CustomizeException;
import com.chh.setup.dao.ArticleDao;
import com.chh.setup.dao.ArticleFavorDao;
import com.chh.setup.dao.CommentDao;
import com.chh.setup.dao.CommentFavorDao;
import com.chh.setup.dto.req.Record;
import com.chh.setup.enums.FavorStateEnum;
import com.chh.setup.model.ArticleFavorModel;
import com.chh.setup.model.CommentFavorModel;
import com.chh.setup.model.CommentModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户点赞操作服务层
 *
 * @author chh
 * @date 2020/2/5 19:17
 */
@Service
public class FavorService {

    @Autowired
    private ArticleFavorDao articleFavorDao;

    @Autowired
    private CommentFavorDao commentFavorDao;

    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private CommentDao commentDao;
    
    public Integer getFavorState(Integer articleId, Integer userId) {
        ArticleFavorModel result = articleFavorDao.findByArticleIdAndUserId(articleId, userId);
        return result != null ? result.getState() : 0;
    }
    
    
    public void setCommentFavorState(List<CommentModel> comments, Integer userId) {
        List<Integer> commentIds = comments.stream().map(CommentModel::getId).collect(Collectors.toList());
        Set<Integer> favourCommentIds = new HashSet<>(commentFavorDao.getFavourComments(commentIds, userId));
        comments.forEach(comment -> {
            if (favourCommentIds.contains(comment.getId())) {
                comment.setFavorState(FavorStateEnum.FAVOR.getState());
            }
        });
    }


    @Transactional
    public void createOrUpdateStates(Integer state, Integer articleId, Integer userId) {
        ArticleFavorModel articleFavor = new ArticleFavorModel();
        articleFavor.setUpdateTime(new Date());
        articleFavor.setArticleId(articleId);
        articleFavor.setUserId(userId);
        articleFavor.setState(state);
        articleFavorDao.save(articleFavor);
        if (state == FavorStateEnum.FAVOR.getState()) {
            articleDao.incLikeCount(articleId, 1);
        } else if (state == FavorStateEnum.CANCEL_FAVOR.getState()) {
            articleDao.incLikeCount(articleId, -1);
        } else {
            throw new CustomizeException(CustomizeErrorCode.PARAM_ERROR);
        } 
    }
    
    
    @Transactional
    public void createOrUpdateStates(List<Record> records, Integer articleId, Integer userId) {
        for (Record record : records) {
            CommentFavorModel commentFavor = new CommentFavorModel();
            commentFavor.setUpdateTime(new Date());
            commentFavor.setCommentId(record.getCommentId());
            commentFavor.setUserId(userId);
            commentFavor.setArticleId(articleId);
            commentFavor.setState(record.getState());
            commentFavorDao.save(commentFavor);
            if (record.getState() == FavorStateEnum.FAVOR.getState()) {
                commentDao.incLikeCount(record.getCommentId(), 1);
            } else if (record.getState() == FavorStateEnum.CANCEL_FAVOR.getState()) {
                commentDao.incLikeCount(record.getCommentId(), -1);
            } else {
                throw new CustomizeException(CustomizeErrorCode.PARAM_ERROR);
            } 
        }
    }
}
