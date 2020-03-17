package com.chh.setup.controller;

import com.chh.setup.advice.exception.CustomizeErrorCode;
import com.chh.setup.advice.exception.JumpExcetion;
import com.chh.setup.model.ArticleModel;
import com.chh.setup.model.UserModel;
import com.chh.setup.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

/**
 * @author chh
 * @date 2020/3/15 20:15
 */
@Controller
public class RouteController {

    @Autowired
    private ArticleService articleService;
    
    @GetMapping("/a/{id}")
    public String article(@PathVariable Integer id) {
        return "/article.html";
    }

    @GetMapping("/u/{id}")
    public String user(@PathVariable Integer id) {
        return "/user.html";
    }

    @GetMapping("/edit")
    public String publish() {
        return "edit.html";
    }

    @GetMapping("/edit/{id}")
    public String edit(HttpServletRequest request,
                       @PathVariable(value = "id") Integer id) {
        ArticleModel article = articleService.getEntityById(id);
        UserModel user = (UserModel) request.getSession().getAttribute("user");
        if (article == null) {
            throw new JumpExcetion(CustomizeErrorCode.ARTICLE_NOT_FOUND);
        } else if (user == null || !article.getAuthorId().equals(user.getId())) {
            throw new JumpExcetion(CustomizeErrorCode.EDIT_PERMISSION_DENY);
        }
        return "/edit.html";
    }

    @GetMapping({"/my-notice/unread", "/my-notice/all"})
    public String notice() {
        return "/notice.html";
    }
}
