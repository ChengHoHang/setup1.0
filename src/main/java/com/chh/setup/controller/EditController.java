package com.chh.setup.controller;

import com.chh.setup.dto.ArticleParam;
import com.chh.setup.dto.ResultDto;
import com.chh.setup.entity.UserEntity;
import com.chh.setup.exception.CustomizeErrorCode;
import com.chh.setup.exception.CustomizeException;
import com.chh.setup.service.ArticleService;
import com.chh.setup.service.TagService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author chh
 * @date 2020/1/10 14:17
 */
@Controller
public class EditController {

    @Autowired
    ArticleService articleService;

    @GetMapping("/edit")
    public String publish() {
        return "edit.html";
    }
    
    @GetMapping("/edit/*")
    public String edit() {
        return "/edit.html";
    }
    
    @PostMapping("/publish/article")
    @ResponseBody
    public Object publishArticle(@RequestBody ArticleParam articleParam,
                                 HttpServletRequest request) {
        //防止用户输入长型的空字符串"          "
        if (StringUtils.isBlank(articleParam.getTitle()) || "".equals(StringUtils.trim(articleParam.getTitle()))) {
            throw new CustomizeException(CustomizeErrorCode.BLANK_TITLE);
        }
        if (StringUtils.isBlank(articleParam.getDescription()) || "".equals(StringUtils.trim(articleParam.getDescription()))) {
            throw new CustomizeException(CustomizeErrorCode.BLANK_DESCRIPTION);
        }
        if (StringUtils.isBlank(articleParam.getType())) {
            throw new CustomizeException(CustomizeErrorCode.BLANK_TYPE);
        }
        if (StringUtils.isBlank(StringUtils.replace(articleParam.getTag(), "|", ""))) {
            throw new CustomizeException(CustomizeErrorCode.BLANK_TAG);
        }
        UserEntity user = (UserEntity) request.getSession().getAttribute("user");
        if (user == null || articleParam.getCreator() == null) {
            throw new CustomizeException(CustomizeErrorCode.USER_LOG_OUT);
        }
        if (articleParam.getId() != null && !articleParam.getCreator().equals(user.getId())) {
            throw new CustomizeException(CustomizeErrorCode.EDIT_PERMISSION_DENY);
        }
        articleService.createOrUpdate(articleParam);
        return ResultDto.okOf(null);
    }

    @GetMapping("/tags")
    @ResponseBody
    public Object getTags() {
        return TagService.showTags();
    }
}
