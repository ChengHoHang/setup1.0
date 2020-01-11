package com.chh.setup.controller;

import com.chh.setup.dto.ArticleDto;
import com.chh.setup.dto.ResultDto;
import com.chh.setup.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author chh
 * @date 2020/1/11 17:42
 */
@RestController
public class ArticleController {

    @Autowired
    ArticleService articleService;

    @GetMapping("/articles")
    public Object allArticles() {
        List<ArticleDto> articles = articleService.listArticle();
        articles.forEach(ArticleDto::showOnHome);
        return ResultDto.okOf(articles);
    }
}
