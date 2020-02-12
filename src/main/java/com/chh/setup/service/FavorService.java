package com.chh.setup.service;

import com.chh.setup.dto.CommentDto;
import com.chh.setup.dto.muilty.Record;
import com.chh.setup.entity.ArticleFavorEntity;
import com.chh.setup.entity.CommentFavorEntity;
import com.chh.setup.repository.ArticleFavorRepository;
import com.chh.setup.repository.ArticleRepository;
import com.chh.setup.repository.CommentFavorRepository;
import com.chh.setup.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    ArticleFavorRepository articleFavorRepository;

    @Autowired
    CommentFavorRepository commentFavorRepository;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    CommentRepository commentRepository;

    /**
     * 判断当前user是否点赞该id文章
     *
     * @param articleId
     * @param userId
     * @return
     */
    public Integer getFavorState(Integer articleId, Integer userId) {
        ArticleFavorEntity result = articleFavorRepository.findByArticleIdAndUserId(articleId, userId);
        return result != null ? result.getState() : 0;
    }

    /**
     * 计算每条commentDto的favorState字段
     * @param comments 一篇文章下的评论列表
     * @param userId   userId
     */
    public void setCommentFavorState(List<CommentDto> comments, Integer userId) {
        List<Integer> commentIds = comments.stream().map(CommentDto::getId).collect(Collectors.toList());
        Set<Integer> favourCommentIds = new HashSet<>(commentFavorRepository.getFavourComments(commentIds, userId));
        if (favourCommentIds.size() == 0) {
            return;
        }
        comments.forEach(comment -> {
            if (favourCommentIds.contains(comment.getId())) {
                comment.setFavorState(1);
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
        articleRepository.incLikeCount(articleId, state == 1 ? 1 : -1);
        ArticleFavorEntity articleFavor = new ArticleFavorEntity();
        articleFavor.setArticleId(articleId);
        articleFavor.setUserId(userId);
        articleFavor.setState(state);
        articleFavor.setGmtModified(System.currentTimeMillis());
        articleFavorRepository.save(articleFavor);
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
            commentRepository.incLikeCount(record.getCommentId(), record.getState() == 1 ? 1 : -1);
            CommentFavorEntity commentFavor = new CommentFavorEntity();
            commentFavor.setCommentId(record.getCommentId());
            commentFavor.setUserId(userId);
            commentFavor.setArticleId(articleId);
            commentFavor.setState(record.getState());
            commentFavor.setGmtModified(System.currentTimeMillis());
            commentFavorRepository.save(commentFavor);
        }
    }
}
