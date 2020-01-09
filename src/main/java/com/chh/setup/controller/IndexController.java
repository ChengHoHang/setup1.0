package com.chh.setup.controller;

import com.chh.setup.Repository.UserRepository;
import com.chh.setup.entity.UserEntity;
import com.chh.setup.service.AuthorizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    private static final String COOKIE_NAME = "token";

    @Autowired
    AuthorizeService authorizeService;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/")
    public String index(HttpServletRequest request) {
        String token = authorizeService.getCookieValue(request, COOKIE_NAME);
        if (token != null) {
            UserEntity user = userRepository.findByToken(token);
            if (user != null) {
                request.getSession().setAttribute("user", user);
            }
        }
        return "index.html";
    }
    
}
