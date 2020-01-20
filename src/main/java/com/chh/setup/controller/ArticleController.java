package com.chh.setup.controller;

import com.chh.setup.dto.ArticleDto;
import com.chh.setup.dto.PagesDto;
import com.chh.setup.dto.ResultDto;
import com.chh.setup.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chh
 * @date 2020/1/11 17:42
 */
@RestController
public class ArticleController {
    
    @Autowired
    ArticleService articleService;

    @Value("${page.size}")
    Integer size;
    
    /**
     * 将分页数据交给前端在首页渲染，提供如下参数
     * article 新闻内容
     * page
     * @return
     */
    @GetMapping("/articles")
    public Object allArticles(@RequestParam(value = "type", required = false) Integer type,
                              @RequestParam(value = "page", required = false, defaultValue = "1") Integer page) {
        PagesDto<ArticleDto> pagesDto = articleService.listByType(page, size, type);
        return ResultDto.okOf(pagesDto);
    }
}
