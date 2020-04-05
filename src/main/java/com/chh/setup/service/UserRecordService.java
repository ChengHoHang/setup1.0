package com.chh.setup.service;

import com.chh.setup.advice.exception.CustomizeErrorCode;
import com.chh.setup.advice.exception.CustomizeException;
import com.chh.setup.dto.res.PagesDto;
import com.chh.setup.dto.res.process.PageSuperDto;
import com.chh.setup.enums.UserRecordEnum;
import com.chh.setup.model.ArticleModel;
import com.chh.setup.model.CommentModel;
import com.chh.setup.model.UserModel;
import com.chh.setup.myutils.PageUtils;
import com.chh.setup.dao.ArticleFavorDao;
import com.chh.setup.dao.ArticleDao;
import com.chh.setup.dao.CommentDao;
import com.chh.setup.dao.UserDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户主页服务层
 *
 * @author chh
 * @date 2020/1/20 16:19
 */
@Service
public class UserRecordService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private ArticleFavorDao articleFavorDao;

    @Value("${page.size}")
    Integer pageSize;

    public UserModel findById(int userId) {
        return userDao.findById(userId).orElse(null);
    }

    public List<ArticleModel> getArticleDtos(Integer authorId, Integer page) {
        PageRequest pageRequest = PageUtils.getDefaultPageRequest(page, pageSize, "updateTime", "likeCount");
        List<ArticleModel> articles = articleDao.findAllByAuthorId(authorId, pageRequest).getContent();
        articles.forEach(article -> {
            article.setTags(StringUtils.split(article.getTag(), " ", 2));
        });
        return articles;
    }

    public Long getCountByAuthorId(Integer authorId) {
        Long count = articleDao.countByAuthorId(authorId);
        return (long) Math.ceil((double) count / pageSize);
    }

    public List<CommentModel> getCommentDtos(Integer userId, Integer page) {
        PageRequest pageRequest = PageUtils.getDefaultPageRequest(page, pageSize, "likeCount", "updateTime");
        List<CommentModel> contents = commentDao.findAllByCommentatorId(userId, pageRequest).getContent();
        contents.forEach(content -> content.setArticleTitle(articleDao.findById(content.getArticleId()).get().getTitle()));
        return contents;
    }

    public Long getCountByCommentatorId(Integer commentatorId) {
        Long count = commentDao.countByCommentatorId(commentatorId);
        return (long) Math.ceil((double) count / pageSize);
    }

    public List<ArticleModel> getFavorArticleDtos(Integer userId, Integer page) {
        PageRequest pageRequest = PageUtils.getDefaultPageRequest(page, pageSize, "updateTime");
        return articleFavorDao.findAllByUserIdAndState(userId, 1, pageRequest).getContent();
    }

    public Long getFavorCountByUser(Integer userId) {
        Long count = articleFavorDao.countByUserIdAndState(userId, 1);
        return (long) Math.ceil((double) count / pageSize);
    }

    /**
     * 用户个人资料页面分页展示
     *
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
            totalPage = getCountByAuthorId(userId);
        } else if (action.equals(UserRecordEnum.COMMENT.getAction())) {
            dataDtos = getCommentDtos(userId, page);
            totalPage = getCountByCommentatorId(userId);
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
