package com.chh.setup.controller;

import com.chh.setup.Repository.UserRepository;
import com.chh.setup.dto.ResultDto;
import com.chh.setup.entity.UserEntity;
import com.chh.setup.service.AuthorizeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class AuthorizeController {

    @Autowired
    AuthorizeService authorizeService;

    @Autowired
    UserRepository userRepository;

    @ResponseBody
    @PostMapping("/login")
    public Object login(@RequestBody Map<String, String> param,
                        HttpServletResponse response) {
        UserEntity user = authorizeService.loginCheck(param);
        if (user != null) {
            UserEntity updateUser = authorizeService.updateUser(user, response);
            return ResultDto.okOf(updateUser);
        } else {
            return ResultDto.errorOf(2001, "您输入的账号不存在或者密码错误，请重试！");
        }
    }

    @ResponseBody
    @PostMapping("/register")
    public Object register(@RequestBody Map<String, String> param) {
        String account = param.get("account");
        String passWord = param.get("passWord");
        UserEntity user;
        if (StringUtils.isBlank(account) && StringUtils.isBlank(passWord)) {
            return ResultDto.errorOf(2002, "账号或者密码不能为空！");
        } else {
            user = userRepository.findByAccount(account);
            if (user != null) {
                return ResultDto.errorOf(2003, "该账号已存在，请重新设置账号！");
            } else {
                user = new UserEntity();
                user.setGmtModified(System.currentTimeMillis());
                user.setGmtCreated(System.currentTimeMillis());
                user.setPassWord(passWord);
                user.setName(param.get("name"));
                user.setAccount(account);
                userRepository.save(user);
                return ResultDto.okOf(null);
            }
        }
    }
}
