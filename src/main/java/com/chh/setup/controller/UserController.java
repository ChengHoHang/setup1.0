package com.chh.setup.controller;

import com.chh.setup.dto.ArticleDto;
import com.chh.setup.dto.ResultDto;
import com.chh.setup.dto.UserRecordDto;
import com.chh.setup.entity.UserEntity;
import com.chh.setup.exception.CustomizeErrorCode;
import com.chh.setup.exception.CustomizeException;
import com.chh.setup.repository.UserRepository;
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

    @Autowired
    UserRepository userRepository;
    
    @GetMapping("/u/*")
    public String user() {
        return "/user.html";
    }

    @GetMapping("/user/{userId}/article")
    @ResponseBody
    public Object getUserRecordById(@PathVariable(name = "userId") Integer userId,
                                    @RequestParam(value = "page", required = false, defaultValue = "1") Integer page) {
        UserEntity user = userRecordService.findById(userId).orElse(null);
        if (user == null) {
            throw new CustomizeException(CustomizeErrorCode.USER_NOT_EXIST);
        }
        UserRecordDto<ArticleDto> userRecordDto = userRecordService.listArticle(user, page);
        return ResultDto.okOf(userRecordDto);
    }
    
}
