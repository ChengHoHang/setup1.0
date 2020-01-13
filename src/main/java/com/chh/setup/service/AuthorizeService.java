package com.chh.setup.service;

import com.chh.setup.repository.UserRepository;
import com.chh.setup.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;

@Service
public class AuthorizeService {

    @Autowired
    UserRepository userRepository;

    public UserEntity loginCheck(Map<String, String> param) {
        UserEntity user = userRepository.findByAccount(param.get("account"));
        if (user != null && user.getPassWord().equals(param.get("passWord"))) {
            return user;
        }
        return null;
    }

    public UserEntity updateUser(UserEntity user, HttpServletResponse response) {
        String token = UUID.randomUUID().toString();
        user.setToken(token);
        user.setGmtModified(System.currentTimeMillis());
        response.addCookie(new Cookie("token", token));
        userRepository.save(user);
        return user;
    }


    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        for (Cookie cookie : cookies) {
            if (cookieName.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
