package com.chh.setup.service;

import com.chh.setup.advice.exception.CustomizeErrorCode;
import com.chh.setup.advice.exception.CustomizeException;
import com.chh.setup.dto.req.ArticleParam;
import com.chh.setup.dto.res.PagesDto;
import com.chh.setup.model.ArticleModel;
import com.chh.setup.model.CategoryModel;
import com.chh.setup.model.CommentModel;
import com.chh.setup.model.UserModel;
import com.chh.setup.myutils.PageUtils;
import com.chh.setup.repository.ArticleRepository;
import com.chh.setup.repository.CategoryRepository;
import com.chh.setup.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
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
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CommentService commentService;

    @Autowired
    private FavorService favorService;
    
    @Transactional
    public void createOrUpdate(ArticleParam articleParam) {
        if (categoryRepository.findById(articleParam.getCategoryId()).orElse(null) == null) {
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
        articleRepository.save(article);
    }
    
    public PagesDto listByType(Integer page, Integer size, Integer categoryId) {
        PageRequest pageRequest = PageUtils.getDefaultPageRequest(page, size, "updateTime");
        List<ArticleModel> contents;
        if (categoryId == null) {
            contents = articleRepository.findAll(pageRequest).getContent();
        } else {
            if (categoryRepository.findById(categoryId).orElse(null) == null) {
                throw new CustomizeException(CustomizeErrorCode.CATEGORY_NOT_EXISIT);
            }
            contents = articleRepository.findAllByCategoryId(categoryId, pageRequest).getContent();
        }
        contents.forEach(content -> {
            content.setDescription(StringUtils.truncate(content.getDescription(), 150) + ".....");
            content.setTags(Arrays.stream(StringUtils.split(content.getTag(), " ")).limit(2).toArray(String[]::new));
            content.setAuthor(userRepository.findById(content.getAuthorId()).get());
        });
        PagesDto pagesDto = new PagesDto();
        pagesDto.setData(contents);
        pagesDto.setTotalPage(getCountByType(categoryId, size));
        return pagesDto;
    }
    
    public Long getCountByType(Integer type, Integer size) {
        long count = type == null ? articleRepository.count() : articleRepository.countByCategoryId(type);
        return (long) Math.ceil((double) count / size);
    }

    public ArticleModel getEntityById(Integer id) {
        return articleRepository.findById(id).orElse(null);
    }
    
    @Transactional
    public void incViewCount(Integer articleId) {
        articleRepository.incViewCount(articleId, 1);
    }

    public ArticleModel getDtoModel(ArticleModel articleModel, HttpServletRequest request) {
        List<CommentModel> comments = articleModel.getCommentCount() != 0 ? commentService.getCommentsByArticleId(articleModel.getId()) : null;
        UserModel user = (UserModel) request.getSession().getAttribute("user");
        if (user != null) {
            articleModel.setFavorState(favorService.getFavorState(articleModel.getId(), user.getId()));
            if (comments != null) {
                favorService.setCommentFavorState(comments, user.getId());
            }
        }
        articleModel.setAuthor(userRepository.findById(articleModel.getAuthorId()).get());
        articleModel.setTags(StringUtils.split(articleModel.getTag(), " "));
        articleModel.setComments(comments);
        List<Object[]> relatedArticles = getRelatedArticle(articleModel.getId(), articleModel.getTags());
        articleModel.setRelatedArticle(relatedArticles);
        return articleModel;
    }

    public List<Object[]> getRelatedArticle(Integer id, String[] tags) {
        if (tags == null) {
            return new ArrayList<>();
        }
        return articleRepository.getRelatedArticleById(id, StringUtils.join(tags, '|'), 0, 15);
    }

    public List<CategoryModel> selectAll() {
        return categoryRepository.findAll();
    }
}
