package com.chh.setup.controller;

import com.chh.setup.dto.res.PagesDto;
import com.chh.setup.dto.res.ResultDto;
import com.chh.setup.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author chh
 * @date 2020/3/15 20:08
 */
@Controller
@RequestMapping("/index")
public class IndexController {

    @Autowired
    private ArticleService articleService;

    @Value("${page.size}")
    private Integer size;
    
    @GetMapping("/static")
    @ResponseBody
    public Object getCategory() {
        return articleService.getCategoryModels();
    }

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
        PagesDto pagesDto = articleService.listByType(page, size, type == 0 ? null : type);
        return ResultDto.okOf(pagesDto);
    }
}
