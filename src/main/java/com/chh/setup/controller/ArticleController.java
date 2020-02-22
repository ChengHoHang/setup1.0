package com.chh.setup.controller;

import com.chh.setup.dto.ArticleDto;
import com.chh.setup.dto.CommentDto;
import com.chh.setup.dto.PagesDto;
import com.chh.setup.dto.ResultDto;
import com.chh.setup.entity.UserEntity;
import com.chh.setup.service.ArticleService;
import com.chh.setup.service.CommentService;
import com.chh.setup.service.FavorService;
import com.chh.setup.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * @author chh
 * @date 2020/1/11 17:42
 */
@Controller
public class ArticleController {

    @Autowired
    ArticleService articleService;

    @Autowired
    CommentService commentService;

    @Autowired
    FavorService favorService;
    
    @Value("${page.size}")
    Integer size;

    /**
     * 将分页数据交给前端在首页渲染，提供如下参数
     * article 新闻内容
     * page 页码
     * @return
     */
    @GetMapping("/articles")
    @ResponseBody
    public Object allArticles(@RequestParam(value = "type", required = false) Integer type,
                              @RequestParam(value = "page", required = false, defaultValue = "1") Integer page) {
        PagesDto<ArticleDto> pagesDto = articleService.listByType(page, size, type);
        return ResultDto.okOf(pagesDto);
    }

    @GetMapping("/a/{id}")
    public String article(@PathVariable("id") Integer id) {
        articleService.incViewCount(id);
        return "/article.html";
    }

    @GetMapping("/article/{id}")
    @ResponseBody
    public Object getArticleById(@PathVariable("id") Integer articleId, HttpServletRequest request) {
        ArticleDto article = articleService.getDtoById(articleId);
        List<CommentDto> comments = article.getCommentCount() != 0 ? commentService.getCommentsByArticleId(articleId) : null;
        UserEntity user = (UserEntity) request.getSession().getAttribute("user");
        if (user != null) {
            article.setFavorState(favorService.getFavorState(articleId, user.getId()));
            if (comments != null) {
                favorService.setCommentFavorState(comments, user.getId());
            }
        }
        article.setComments(comments);
        List<Object[]> relatedArticles = articleService.getRelatedArticle(articleId, article.getTags());
        article.setRelatedArticle(relatedArticles);
        article.setTags(Arrays.stream(article.getTags()).map(tag -> TagService.getIdMap().get(tag)).toArray(String[]::new));
        return ResultDto.okOf(article);
    }
    
}
