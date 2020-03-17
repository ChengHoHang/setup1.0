package com.chh.setup.controller;

import com.chh.setup.advice.exception.CustomizeErrorCode;
import com.chh.setup.advice.exception.JumpExcetion;
import com.chh.setup.dto.res.PagesDto;
import com.chh.setup.dto.res.ResultDto;
import com.chh.setup.dto.res.UserRecordDto;
import com.chh.setup.model.UserModel;
import com.chh.setup.service.UserRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 
 * @author chh
 * @date 2020/1/19 16:15
 */
@RestController
@RequestMapping("/user")
public class UserIndexController {
    
    @Autowired
    private UserRecordService userRecordService;

    @GetMapping("/{userId}")
    public Object getHomeUser(@PathVariable(name = "userId") Integer userId) {
        UserModel user = userRecordService.findById(userId);
        if (user == null) {
            throw new JumpExcetion(CustomizeErrorCode.USER_NOT_EXIST);
        }
        return ResultDto.okOf(user);
    }

    @GetMapping("/{userId}/{action}")
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
