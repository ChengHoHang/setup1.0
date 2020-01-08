package com.chh.setup.controller;

import com.chh.setup.dto.ResultDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class AuthorizeController {

    @ResponseBody
    @PostMapping("/login")
    public Object login(@RequestBody Map<String, String> param) {
        if ("chh123".equals(param.get("account")) && "123456".equals(param.get("passWord"))) { //数据库校验
            return ResultDto.okOf(null);
        } else {
            return ResultDto.errorOf(2001, "您输入的账号或者密码错误，请重试！");
        }
    }

    @ResponseBody
    @PostMapping("/register")
    public Object register(@RequestBody Map<String, String> param) {
        return null;
    }
}
