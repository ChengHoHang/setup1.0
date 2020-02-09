package com.chh.setup.controller;

import com.chh.setup.exception.CustomizeErrorCode;
import com.chh.setup.exception.CustomizeException;
import com.chh.setup.repository.UserRepository;
import com.chh.setup.dto.ResultDto;
import com.chh.setup.entity.UserEntity;
import com.chh.setup.service.AuthorizeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class AuthorizeController {
    
    @Value("${app.cookie.name}")
    String COOKIE_NAME;
    
    @Autowired
    AuthorizeService authorizeService;

    @Autowired
    UserRepository userRepository;

    @ResponseBody
    @PostMapping("/login")
    public Object login(@RequestBody Map<String, String> param,
                        HttpServletRequest request,
                        HttpServletResponse response) {
        UserEntity user = authorizeService.loginCheck(param);
        if (user != null) {
            UserEntity updateUser = authorizeService.updateUser(user, response);
            request.getSession().setAttribute("user", user);
            return ResultDto.okOf(updateUser);
        } else {
            throw new CustomizeException(CustomizeErrorCode.REGISTER_ACCOUNT_ABNORMAL);
        }
    }

    @GetMapping("/test_login")
    @ResponseBody
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

    @ResponseBody
    @PostMapping("/register")
    public Object register(@RequestBody Map<String, String> param) {
        String account = param.get("account");
        String passWord = param.get("passWord");
        UserEntity user;
        if (StringUtils.isBlank(account) && StringUtils.isBlank(passWord)) {
            throw new CustomizeException(CustomizeErrorCode.REGISTER_PARAM_ERROR);
        } else {
            user = userRepository.findByAccount(account);
            if (user != null) {
                throw new CustomizeException(CustomizeErrorCode.REGISTER_ACCOUNT_EXIST);
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

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().removeAttribute("user");
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
