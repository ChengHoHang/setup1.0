package com.chh.setup.service;

import com.chh.setup.dto.CommentDto;
import com.chh.setup.dto.CommentParam;
import com.chh.setup.entity.ArticleEntity;
import com.chh.setup.entity.CommentEntity;
import com.chh.setup.entity.UserEntity;
import com.chh.setup.exception.CustomizeErrorCode;
import com.chh.setup.exception.CustomizeException;
import com.chh.setup.myutils.DateUtils;
import com.chh.setup.repository.ArticleRepository;
import com.chh.setup.repository.CommentRepository;
import com.chh.setup.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author chh
 * @date 2020/2/4 19:13
 */
@Service
public class CommentService {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ArticleRepository articleRepository;

    /**
     * 先搞新建，编辑功能有时间再搞
     * @param commentParam
     */
    @Transactional
    public void createOrUpdate(CommentParam commentParam) {
        UserEntity user = userRepository.findById(commentParam.getCommentator()).orElse(null);
        if (user == null) {
            throw new CustomizeException(CustomizeErrorCode.USER_NOT_EXIST);
        }
        ArticleEntity article = articleRepository.findById(commentParam.getArticleId()).orElse(null);
        if (article == null) {
            throw new CustomizeException(CustomizeErrorCode.ARTICLE_NOT_FOUND);
        }
        CommentEntity comment = new CommentEntity();
        BeanUtils.copyProperties(commentParam, comment);
        comment.setGmtCreated(System.currentTimeMillis());
        comment.setGmtModified(System.currentTimeMillis());
        commentRepository.save(comment);
        articleRepository.incCommentCount(commentParam.getArticleId(), 1);
    }

    /**
     * 获取文章id下的所有评论
     * @param id 文章id
     * @return 对应文章id下的所有评论
     */
    public List<CommentDto> getCommentsByArticleId(Integer id) {
        List<CommentEntity> dbComments = commentRepository.findByArticleIdOrderByGmtModified(id);
        List<Integer> userIds = dbComments.stream().map(CommentEntity::getCommentator).distinct().collect(Collectors.toList());
        List<UserEntity> users = userRepository.findAllByIds(userIds);
        Map<Integer, UserEntity> userMap = users.stream().collect(Collectors.toMap(UserEntity::getId, user -> user));
        return dbComments.stream().map(comment -> {
            CommentDto commentDto = new CommentDto();
            BeanUtils.copyProperties(comment, commentDto);
            commentDto.setCommentator(userMap.get(comment.getCommentator()));
            commentDto.setGmtCreated(DateUtils.timestamp2Date(comment.getGmtCreated(), "yyyy-MM-dd HH:mm"));
            return commentDto;
        }).collect(Collectors.toList());
    }
    
}
