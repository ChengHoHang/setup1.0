package com.chh.setup.controller;

import com.chh.setup.advice.exception.CustomizeErrorCode;
import com.chh.setup.dto.res.ResultDto;
import com.chh.setup.service.ArticleService;
import com.chh.setup.service.SearchService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @author chh
 * @date 2020/3/23 17:16
 */
@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @Autowired
    private ArticleService articleService;
    
    @GetMapping("/article")
    public Object searchArticle(@RequestParam(value = "keyword", required = false) String keyword,
                                @RequestParam(value = "categoryIds", required = false) String categoryIds,
                                @RequestParam(value = "page", required = false, defaultValue = "1") Integer page) throws IOException {
        if ("".equals(StringUtils.trim(keyword))) {
            return ResultDto.errorOf(CustomizeErrorCode.MEANINGLESS_KEYWORD);
        }
        return ResultDto.okOf(searchService.searchByKeyword(keyword, categoryIds, page)); 
    }
    
    @GetMapping("/static")
    @ResponseBody
    public Object getCategory() {
        return articleService.selectAll();
    }
}
