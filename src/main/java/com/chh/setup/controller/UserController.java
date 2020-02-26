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
    
    @GetMapping("/u/*")
    public String user() {
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

    @GetMapping("/user/{userId}/{action}")
    @ResponseBody
    public Object getUserRecord(@PathVariable(name = "userId") Integer userId,
                                @PathVariable(name = "action") String action,
                                @RequestParam(value = "page", required = false, defaultValue = "1") Integer page) {
        PagesDto pagesDto = userRecordService.getMyRecord(userId, action, page);
        UserRecordDto userRecordDto = new UserRecordDto();
        userRecordDto.setData(pagesDto);
        userRecordDto.setUserId(userId);
        userRecordDto.setRecordType(action);
        return ResultDto.okOf(userRecordDto);
    }
}
