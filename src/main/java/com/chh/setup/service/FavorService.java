package com.chh.setup.service;

import com.chh.setup.advice.exception.CustomizeErrorCode;
import com.chh.setup.advice.exception.CustomizeException;
import com.chh.setup.dto.req.Record;
import com.chh.setup.enums.FavorStateEnum;
import com.chh.setup.model.ArticleFavorModel;
import com.chh.setup.model.CommentFavorModel;
import com.chh.setup.model.CommentModel;
import com.chh.setup.repository.ArticleFavorRepository;
import com.chh.setup.repository.ArticleRepository;
import com.chh.setup.repository.CommentFavorRepository;
import com.chh.setup.repository.CommentRepository;
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
    private ArticleFavorRepository articleFavorRepository;

    @Autowired
    private CommentFavorRepository commentFavorRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CommentRepository commentRepository;

    /**
     * 判断当前user是否点赞该id文章
     *
     * @param articleId
     * @param userId
     * @return
     */
    public Integer getFavorState(Integer articleId, Integer userId) {
        ArticleFavorModel result = articleFavorRepository.findByArticleIdAndUserId(articleId, userId);
        return result != null ? result.getState() : 0;
    }

    /**
     * 计算每条commentDto的favorState字段
     * @param comments 一篇文章下的评论列表
     * @param userId   userId
     */
    public void setCommentFavorState(List<CommentModel> comments, Integer userId) {
        List<Integer> commentIds = comments.stream().map(CommentModel::getId).collect(Collectors.toList());
        Set<Integer> favourCommentIds = new HashSet<>(commentFavorRepository.getFavourComments(commentIds, userId));
        comments.forEach(comment -> {
            if (favourCommentIds.contains(comment.getId())) {
                comment.setFavorState(FavorStateEnum.FAVOR.getState());
            }
        });
    }

    /**
     * 更新article表点赞数，插入或更新favor表点赞记录
     *
     * @param state     0(取消点赞) or 1(点赞)
     * @param articleId 文章id
     * @param userId
     */
    @Transactional
    public void createOrUpdateStates(Integer state, Integer articleId, Integer userId) {
        ArticleFavorModel articleFavor = new ArticleFavorModel();
        articleFavor.setUpdateTime(new Date());
        articleFavor.setArticleId(articleId);
        articleFavor.setUserId(userId);
        articleFavor.setState(state);
        articleFavorRepository.save(articleFavor);
        if (state == FavorStateEnum.FAVOR.getState()) {
            articleRepository.incLikeCount(articleId, 1);
        } else if (state == FavorStateEnum.CANCEL_FAVOR.getState()) {
            articleRepository.incLikeCount(articleId, -1);
        } else {
            throw new CustomizeException(CustomizeErrorCode.PARAM_ERROR);
        } 
    }

    /**
     * 更新comment表点赞数，插入或更新favor表点赞记录
     *
     * @param records   用户点赞评论的更新记录
     * @param articleId 文章id
     * @param userId    用户id
     */
    @Transactional
    public void createOrUpdateStates(List<Record> records, Integer articleId, Integer userId) {
        for (Record record : records) {
            CommentFavorModel commentFavor = new CommentFavorModel();
            commentFavor.setUpdateTime(new Date());
            commentFavor.setCommentId(record.getCommentId());
            commentFavor.setUserId(userId);
            commentFavor.setArticleId(articleId);
            commentFavor.setState(record.getState());
            commentFavorRepository.save(commentFavor);
            if (record.getState() == FavorStateEnum.FAVOR.getState()) {
                commentRepository.incLikeCount(record.getCommentId(), 1);
            } else if (record.getState() == FavorStateEnum.CANCEL_FAVOR.getState()) {
                commentRepository.incLikeCount(record.getCommentId(), -1);
            } else {
                throw new CustomizeException(CustomizeErrorCode.PARAM_ERROR);
            } 
        }
    }
}
