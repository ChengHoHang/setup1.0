package com.chh.setup.service;

import com.chh.setup.advice.exception.CustomizeErrorCode;
import com.chh.setup.advice.exception.CustomizeException;
import com.chh.setup.dto.req.ArticleParam;
import com.chh.setup.dto.res.PagesDto;
import com.chh.setup.model.*;
import com.chh.setup.myutils.PageUtils;
import com.chh.setup.dao.ArticleDao;
import com.chh.setup.dao.CategoryDao;
import com.chh.setup.dao.RelatedArticleDao;
import com.chh.setup.dao.UserDao;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author chh
 * @date 2020/1/10 21:12
 */
@Service
public class ArticleService {

    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private RelatedArticleDao relatedArticleDao;
    
    @Autowired
    private CommentService commentService;

    @Autowired
    private FavorService favorService;

    @Autowired
    private SearchService searchService;
    
    @Getter
    private List<CategoryModel> categoryModels;

    @PostConstruct
    private void init() {
        categoryModels = categoryDao.findAll();
    }

    @Transactional
    @Async
    public void createOrUpdate(ArticleParam articleParam) throws IOException {
        if (categoryDao.findById(articleParam.getCategoryId()).orElse(null) == null) {
            throw new CustomizeException(CustomizeErrorCode.CATEGORY_NOT_EXISIT);
        }
        ArticleModel article;
        if (articleParam.getArticleId() == null) { // 插入
            article = new ArticleModel();
            article.setCreateTime(new Date());
        } else {  // 更新
            article = getEntityById(articleParam.getArticleId());
            if (article == null) {
                throw new CustomizeException(CustomizeErrorCode.ARTICLE_NOT_FOUND);
            }
        }
        article.setUpdateTime(new Date());
        BeanUtils.copyProperties(articleParam, article, "gmtModified");
        articleDao.save(article);

        List<Integer> relatedIds = searchService.getRelatedArticle(article.getId(), article.getTitle(), article.getTag());
        String relatedStr = StringUtils.join(relatedIds, ",");
        RelatedArticleModel relatedArticleModel = new RelatedArticleModel();
        relatedArticleModel.setArticleId(article.getId());
        relatedArticleModel.setRelatedIds(relatedStr);
        relatedArticleDao.save(relatedArticleModel);
    }

    public PagesDto listByType(Integer page, Integer size, Integer categoryId) {
        PageRequest pageRequest = PageUtils.getDefaultPageRequest(page, size, "updateTime");
        List<ArticleModel> contents;
        if (categoryId == null) {
            contents = articleDao.findAll(pageRequest).getContent();
        } else {
            if (categoryDao.findById(categoryId).orElse(null) == null) {
                throw new CustomizeException(CustomizeErrorCode.CATEGORY_NOT_EXISIT);
            }
            contents = articleDao.findAllByCategoryId(categoryId, pageRequest).getContent();
        }
        contents.forEach(content -> {
            content.setDescription(StringUtils.truncate(content.getDescription(), 150) + ".....");
            content.setTags(Arrays.stream(StringUtils.split(content.getTag(), " ")).limit(2).toArray(String[]::new));
            content.setAuthor(userDao.findById(content.getAuthorId()).get());
        });
        PagesDto pagesDto = new PagesDto();
        pagesDto.setData(contents);
        pagesDto.setTotalPage(getCountByType(categoryId, size));
        return pagesDto;
    }

    public Long getCountByType(Integer type, Integer size) {
        long count = type == null ? articleDao.count() : articleDao.countByCategoryId(type);
        return (long) Math.ceil((double) count / size);
    }

    public ArticleModel getEntityById(Integer id) {
        return articleDao.findById(id).orElse(null);
    }

    @Transactional
    public void incViewCount(Integer articleId) {
        articleDao.incViewCount(articleId, 1);
    }

    public ArticleModel getDtoModel(ArticleModel articleModel, HttpServletRequest request) throws IOException {
        List<CommentModel> comments = articleModel.getCommentCount() != 0 ? commentService.getCommentsByArticleId(articleModel.getId()) : null;
        UserModel user = (UserModel) request.getSession().getAttribute("user");
        if (user != null) {
            articleModel.setFavorState(favorService.getFavorState(articleModel.getId(), user.getId()));
            if (comments != null) {
                favorService.setCommentFavorState(comments, user.getId());
            }
        }
        articleModel.setAuthor(userDao.findById(articleModel.getAuthorId()).get());
        articleModel.setTags(StringUtils.split(articleModel.getTag(), " "));
        articleModel.setComments(comments);
        String[] relatedIds = relatedArticleDao.findById(articleModel.getId()).get().getRelatedIds().split(",");
        List<Object[]> relatedArticles = new ArrayList<>();
        for (String relatedId : relatedIds) {
            String title = articleDao.getTitle(Integer.parseInt(relatedId));
            relatedArticles.add(new Object[]{relatedId, title});
        }
        articleModel.setRelatedArticle(relatedArticles);
        return articleModel;
    }
    
}
