package com.chh.setup.controller;

import com.chh.setup.advice.exception.CustomizeErrorCode;
import com.chh.setup.advice.exception.CustomizeException;
import com.chh.setup.dto.req.ArticleParam;
import com.chh.setup.dto.res.ResultDto;
import com.chh.setup.model.ArticleModel;
import com.chh.setup.model.UserModel;
import com.chh.setup.myutils.NetUtils;
import com.chh.setup.service.ArticleService;
import com.chh.setup.service.TagService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author chh
 * @date 2020/1/10 14:17
 */
@RestController
@RequestMapping("/edit")
public class EditController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private HttpServletRequest request;

    @GetMapping("/tags")
    public Object getTags() {
        return TagService.showTags();
    }

    @GetMapping("/static")
    public Object getStaticRes() {
        return Arrays.asList(articleService.getCategoryModels(), TagService.showTags());
    }

    @PostMapping("/publish/article")
    public Object publishArticle(@Valid @RequestBody ArticleParam articleParam, BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) {
            throw new CustomizeException(CustomizeErrorCode.PARAM_ERROR, NetUtils.processErrorMsg(bindingResult));
        }
        UserModel user = (UserModel) request.getSession().getAttribute("user");
        if (user == null || !user.getId().equals(articleParam.getAuthorId())) {
            throw new CustomizeException(CustomizeErrorCode.USER_LOG_OUT);
        }
        articleService.createOrUpdate(articleParam);
        return ResultDto.okOf(null);
    }

    @GetMapping("/article/{id}")
    public Object getArticleModel(@PathVariable Integer id) {
        ArticleModel article = articleService.getEntityById(id);
        article.setTags(StringUtils.split(article.getTag(), " "));
        return ResultDto.okOf(article);
    }
}
