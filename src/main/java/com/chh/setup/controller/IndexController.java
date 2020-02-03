package com.chh.setup.controller;

import com.chh.setup.dto.ResultDto;
import com.chh.setup.exception.CustomizeErrorCode;
import com.chh.setup.repository.UserRepository;
import com.chh.setup.service.AuthorizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class IndexController {
    
    @Value("${app.cookie.name}")
    String COOKIE_NAME;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/test_login")
    public Object drawUserInfo(HttpServletRequest request) {
        Object user = request.getSession().getAttribute("user");
        if (user != null) {
            return ResultDto.okOf(user);
        }
        String token = AuthorizeService.getCookieValue(request, COOKIE_NAME);
        if (token != null) {
            user = userRepository.findByToken(token);
            if (user != null) {
                request.getSession().setAttribute("user", user);
                return ResultDto.okOf(user);
            }
        }
        return ResultDto.errorOf(CustomizeErrorCode.USER_LOG_OUT);
    }
    
}
