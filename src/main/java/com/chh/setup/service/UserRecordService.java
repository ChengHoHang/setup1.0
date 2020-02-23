package com.chh.setup.service;

import com.chh.setup.dto.ArticleDto;
import com.chh.setup.dto.CommentDto;
import com.chh.setup.dto.PagesDto;
import com.chh.setup.entity.ArticleEntity;
import com.chh.setup.entity.UserEntity;
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

    /**
     * 用户个人资料页面分页展示
     *
     * @param creator
     * @param page
     * @return
     */
    public PagesDto<ArticleDto> getMyArticles(Integer creator, Integer page) {
        PageRequest pageRequest = PageUtils.getDefaultPageRequest(page, pageSize, "gmtModified", "likeCount");
        List<ArticleEntity> articles = articleRepository.findAllByCreator(creator, pageRequest).getContent();
        List<ArticleDto> articleDtos = articles.stream().map(article -> {
            ArticleDto articleDto = new ArticleDto(article);
            articleDto.setTags(
                    Arrays.stream(StringUtils.split(article.getTag(), ","))
                            .limit(2)
                            .map(tag -> TagService.getIdMap().get(tag))
                            .toArray(String[]::new));
            return articleDto;
        }).collect(Collectors.toList());
        PagesDto<ArticleDto> pagesDto = new PagesDto<>();
        pagesDto.setData(articleDtos);
        pagesDto.setTotalPage(getCountByCreator(creator));
        return pagesDto;
    }

    public Long getCountByCreator(Integer creator) {
        Long count = articleRepository.countByCreator(creator);
        return (long) Math.ceil((double) count / pageSize);
    }

    public PagesDto<CommentDto> getMyComments(Integer userId, Integer page) {
        PageRequest pageRequest = PageUtils.getDefaultPageRequest(page, pageSize, "likeCount", "gmtModified");
        List<CommentDto> commentDtos = commentRepository
                .findAllByCommentator(userId, pageRequest)
                .getContent();
        PagesDto<CommentDto> pagesDto = new PagesDto<>();
        pagesDto.setData(commentDtos);
        pagesDto.setTotalPage(getCountByCommentator(userId));
        return pagesDto;
    }

    public Long getCountByCommentator(Integer commentator) {
        Long count = commentRepository.countByCommentator(commentator);
        return (long) Math.ceil((double) count / pageSize);
    }

    public PagesDto<ArticleDto> getMyFavorArticles(Integer userId, Integer page) {
        PageRequest pageRequest = PageUtils.getDefaultPageRequest(page, pageSize, "gmtModified");
        List<ArticleDto> articleDtos = articleFavorRepository.findAllByUserId(userId, pageRequest).getContent();
        PagesDto<ArticleDto> pagesDto = new PagesDto<>();
        pagesDto.setData(articleDtos);
        pagesDto.setTotalPage(getFavorCountByUser(userId));
        return pagesDto;
    }

    public Long getFavorCountByUser(Integer userId) {
        Long count = articleFavorRepository.countByUserIdAndState(userId, 1);
        return (long) Math.ceil((double) count / pageSize);
    }
}
