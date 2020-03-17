package com.chh.setup.controller;

import com.chh.setup.dto.req.LoginParam;
import com.chh.setup.dto.req.RegisterParam;
import com.chh.setup.dto.res.ResultDto;
import com.chh.setup.model.UserModel;
import com.chh.setup.advice.exception.CustomizeErrorCode;
import com.chh.setup.advice.exception.CustomizeException;
import com.chh.setup.myutils.NetUtils;
import com.chh.setup.service.AuthorizeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Controller
@RequestMapping("/authorize")
public class AuthorizeController {
    
    @Value("${app.cookie.name}")
    private String COOKIE_NAME;
    
    @Autowired
    private AuthorizeService authorizeService;

    @ResponseBody
    @PostMapping("/login")
    public Object login(@RequestBody LoginParam param,
                        HttpServletRequest request,
                        HttpServletResponse response) throws NoSuchAlgorithmException {
        if (StringUtils.isBlank(param.getAccount()) || StringUtils.isBlank(param.getPassword())) {
            throw new CustomizeException(CustomizeErrorCode.LOGIN_FAIL);
        }
        UserModel user = authorizeService.login(param);
        if (user != null) {
            authorizeService.addCookie(user, response);
            request.getSession().setAttribute("user", user);
            return ResultDto.okOf(null);
        } else {
            throw new CustomizeException(CustomizeErrorCode.LOGIN_FAIL);
        }
    }

    @GetMapping("/test_login")
    @ResponseBody
    public Object getUserInfo(HttpServletRequest request) {
        Object user = request.getSession().getAttribute("user");
        if (user != null) {
            return ResultDto.okOf(user);
        }
        String token = NetUtils.getCookieValue(request, COOKIE_NAME);
        if (token != null) {
            user = authorizeService.getUserByToken(token);
            if (user != null) {
                request.getSession().setAttribute("user", user);
                return ResultDto.okOf(user);
            }
        }
        return ResultDto.errorOf(CustomizeErrorCode.USER_LOG_OUT);
    }

    @ResponseBody
    @PostMapping("/register")
    public Object register(@Valid @RequestBody RegisterParam param, BindingResult bindingResult) throws NoSuchAlgorithmException {
        if (bindingResult.hasErrors()) {
            throw new CustomizeException(CustomizeErrorCode.REGISTER_PARAM_INVALID, NetUtils.processErrorMsg(bindingResult));
        }
        UserModel user = new UserModel();
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setAccount(param.getAccount());
        user.setPassword(param.getPassWord());
        user.setName(param.getName());
        authorizeService.register(user);
        return ResultDto.okOf(null);
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
            request.getSession().invalidate();
            Cookie cookie = new Cookie("token", null);
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        return "redirect:/";
    }
}
