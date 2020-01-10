package com.chh.setup.controller;

import com.chh.setup.Repository.UserRepository;
import com.chh.setup.dto.ResultDto;
import com.chh.setup.entity.UserEntity;
import com.chh.setup.service.AuthorizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class IndexController {

    private static final String COOKIE_NAME = "token";

    @Autowired
    UserRepository userRepository;

    @GetMapping("/test_login")
    public Object drawUserInfo(HttpServletRequest request) {
        String token = AuthorizeService.getCookieValue(request, COOKIE_NAME);
        if (token != null) {
            UserEntity user = userRepository.findByToken(token);
            if (user != null) {
                request.getSession().setAttribute("user", user);
                return ResultDto.okOf(user);
            }
        }
        ResultDto resultDto = new ResultDto();
        resultDto.setCode(201);
        resultDto.setMessage("未登录状态");
        return resultDto;
    }
    
}
