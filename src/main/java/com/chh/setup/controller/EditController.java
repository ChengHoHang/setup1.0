package com.chh.setup.controller;

import com.chh.setup.repository.ArticleRepository;
import com.chh.setup.dto.ArticleParam;
import com.chh.setup.dto.ResultDto;
import com.chh.setup.service.ArticleService;
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
    ArticleRepository articleRepository;
    
    @Autowired
    ArticleService articleService;
    
    @GetMapping("/edit")
    public String edit() {
        return "edit.html";
    }

    @PostMapping("/publish")
    @ResponseBody
    public Object publish(@RequestBody ArticleParam articleParam,
                          HttpServletRequest request) {
        //防止用户输入长型的空字符串"          "
        if (StringUtils.isBlank(articleParam.getTitle()) || "".equals(StringUtils.trim(articleParam.getTitle()))) {
            return ResultDto.errorOf(3001, "标题不能为空");
        }
        if (StringUtils.isBlank(articleParam.getDescription())  || "".equals(StringUtils.trim(articleParam.getDescription()))) {
            return ResultDto.errorOf(3002, "新闻内容不能为空");
        }
        if (StringUtils.isBlank(articleParam.getType())) {
            return ResultDto.errorOf(3003, "类型不能为空");
        }
        if (StringUtils.isBlank(articleParam.getTag()) || "".equals(StringUtils.trim(articleParam.getTag()))) {
            return ResultDto.errorOf(3004, "标签不能为空");
        }
        if (request.getSession().getAttribute("user") == null || articleParam.getCreator() == null) {
            return ResultDto.errorOf(201, "未登录状态");
        }
        articleService.createOrUpdate(articleParam);
        return ResultDto.okOf(articleParam);
    }
}
