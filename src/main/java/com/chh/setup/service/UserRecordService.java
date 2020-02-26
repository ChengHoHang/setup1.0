package com.chh.setup.service;

import com.chh.setup.dto.ArticleDto;
import com.chh.setup.dto.CommentDto;
import com.chh.setup.dto.PagesDto;
import com.chh.setup.dto.process.PageSuperDto;
import com.chh.setup.entity.ArticleEntity;
import com.chh.setup.entity.UserEntity;
import com.chh.setup.enums.UserRecordEnum;
import com.chh.setup.exception.CustomizeErrorCode;
import com.chh.setup.exception.CustomizeException;
import com.chh.setup.myutils.PageUtils;
import com.chh.setup.repository.ArticleFavorRepository;
import com.chh.setup.repository.ArticleRepository;
import com.chh.setup.repository.CommentRepository;
import com.chh.setup.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 用户主页服务层
 *
 * @author chh
 * @date 2020/1/20 16:19
 */
@Service
public class UserRecordService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    ArticleFavorRepository articleFavorRepository;

    @Value("${page.size}")
    Integer pageSize;

    public Optional<UserEntity> findById(int userId) {
        return userRepository.findById(userId);
    }

    public List<ArticleDto> getArticleDtos(Integer creator, Integer page) {
        PageRequest pageRequest = PageUtils.getDefaultPageRequest(page, pageSize, "gmtModified", "likeCount");
        List<ArticleEntity> articles = articleRepository.findAllByCreator(creator, pageRequest).getContent();
        return articles.stream().map(article -> {
            ArticleDto articleDto = new ArticleDto(article);
            articleDto.setTags(
                    Arrays.stream(StringUtils.split(article.getTag(), "|"))
                            .limit(2)
                            .map(tag -> TagService.getIdMap().get(tag))
                            .toArray(String[]::new));
            return articleDto;
        }).collect(Collectors.toList());
    }

    public Long getCountByCreator(Integer creator) {
        Long count = articleRepository.countByCreator(creator);
        return (long) Math.ceil((double) count / pageSize);
    }

    public List<CommentDto> getCommentDtos(Integer userId, Integer page) {
        PageRequest pageRequest = PageUtils.getDefaultPageRequest(page, pageSize, "likeCount", "gmtModified");
        return commentRepository
                .findAllByCommentator(userId, pageRequest)
                .getContent();
    }

    public Long getCountByCommentator(Integer commentator) {
        Long count = commentRepository.countByCommentator(commentator);
        return (long) Math.ceil((double) count / pageSize);
    }

    public List<ArticleDto> getFavorArticleDtos(Integer userId, Integer page) {
        PageRequest pageRequest = PageUtils.getDefaultPageRequest(page, pageSize, "gmtModified");
        return articleFavorRepository.findAllByUserId(userId, pageRequest).getContent();
    }

    public Long getFavorCountByUser(Integer userId) {
        Long count = articleFavorRepository.countByUserIdAndState(userId, 1);
        return (long) Math.ceil((double) count / pageSize);
    }

    /**
     * 用户个人资料页面分页展示
     * @param userId
     * @param action
     * @param page
     * @return
     */
    public PagesDto getMyRecord(Integer userId, String action, Integer page) {
        Long totalPage;
        List<? extends PageSuperDto> dataDtos;
        if (action.equals(UserRecordEnum.ARTICLE.getAction())) {
            dataDtos = getArticleDtos(userId, page);
            totalPage = getCountByCreator(userId);
        } else if (action.equals(UserRecordEnum.COMMENT.getAction())) {
            dataDtos = getCommentDtos(userId, page);
            totalPage = getCountByCommentator(userId);
        } else if (action.equals(UserRecordEnum.FAVOR.getAction())) {
            dataDtos = getFavorArticleDtos(userId, page);
            totalPage = getFavorCountByUser(userId);
        } else {
            throw new CustomizeException(CustomizeErrorCode.PARAM_ERROR);
        }
        PagesDto pagesDto = new PagesDto();
        pagesDto.setData(dataDtos);
        pagesDto.setTotalPage(totalPage);
        return pagesDto;
    }
}
