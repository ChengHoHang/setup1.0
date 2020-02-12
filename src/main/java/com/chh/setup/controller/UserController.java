package com.chh.setup.controller;

import com.chh.setup.dto.*;
import com.chh.setup.entity.UserEntity;
import com.chh.setup.exception.CustomizeErrorCode;
import com.chh.setup.exception.CustomizeException;
import com.chh.setup.service.UserRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 
 * @author chh
 * @date 2020/1/19 16:15
 */
@Controller
public class UserController {
    
    @Autowired
    UserRecordService userRecordService;
    
    @GetMapping("/u/{id}")
    public String user(@PathVariable(name = "id") Integer Id) {
        return "/user.html";
    }

    @GetMapping("/user/{userId}")
    @ResponseBody
    public Object getHomeUser(@PathVariable(name = "userId") Integer userId) {
        UserEntity user = userRecordService.findById(userId).orElse(null);
        if (user == null) {
            throw new CustomizeException(CustomizeErrorCode.USER_NOT_EXIST);
        }
        return ResultDto.okOf(user);
    }

    @GetMapping("/user/{userId}/article")
    @ResponseBody
    public Object getUserArticle(@PathVariable(name = "userId") Integer userId,
                                     @RequestParam(value = "page", required = false, defaultValue = "1") Integer page) {
        PagesDto<ArticleDto> pagesDto = userRecordService.getMyArticles(userId, page);
        UserRecordDto<ArticleDto> userRecordDto = new UserRecordDto<>();
        userRecordDto.setData(pagesDto);
        userRecordDto.setUserId(userId);
        userRecordDto.setRecordType("article");
        return ResultDto.okOf(userRecordDto);
    }

    @GetMapping("/user/{userId}/comment")
    @ResponseBody
    public Object getUserComment(@PathVariable(name = "userId") Integer userId,
                                     @RequestParam(value = "page", required = false, defaultValue = "1") Integer page) {
        PagesDto<CommentDto> pagesDto = userRecordService.getMyComments(userId, page);
        UserRecordDto<CommentDto> userRecordDto = new UserRecordDto<>();
        userRecordDto.setData(pagesDto);
        userRecordDto.setUserId(userId);
        userRecordDto.setRecordType("comment");
        return ResultDto.okOf(userRecordDto);
    }

    @GetMapping("/user/{userId}/favor")
    @ResponseBody
    public Object getUserFavorArticle(@PathVariable(name = "userId") Integer userId,
                                     @RequestParam(value = "page", required = false, defaultValue = "1") Integer page) {
        PagesDto<ArticleDto> pagesDto = userRecordService.getMyFavorArticles(userId, page);
        UserRecordDto<ArticleDto> userRecordDto = new UserRecordDto<>();
        userRecordDto.setData(pagesDto);
        userRecordDto.setUserId(userId);
        userRecordDto.setRecordType("favor");
        return ResultDto.okOf(userRecordDto);
    }
    
}
