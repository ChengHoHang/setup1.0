package com.chh.setup.service;

import com.chh.setup.advice.exception.CustomizeErrorCode;
import com.chh.setup.advice.exception.CustomizeException;
import com.chh.setup.dto.req.LoginParam;
import com.chh.setup.model.UserModel;
import com.chh.setup.myutils.NetUtils;
import com.chh.setup.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * 用户认证服务层
 * @author chh
 * @date 2020/1/2 16:19
 */
@Service
public class AuthorizeService {

    @Autowired
    private UserRepository userRepository;

    public UserModel login(LoginParam param) throws NoSuchAlgorithmException {
        return userRepository.findByAccountAndPassword(param.getAccount(), NetUtils.encodeByMd5(param.getPassword()));
    }

    @Transactional
    public void addCookie(UserModel user, HttpServletResponse response) {
        String token = UUID.randomUUID().toString();
        user.setToken(token);
        response.addCookie(new Cookie("token", token));
        userRepository.save(user);
    }
    
    @Transactional
    public void register(UserModel user) throws NoSuchAlgorithmException {
        user.setPassword(NetUtils.encodeByMd5(user.getPassword()));
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            throw new CustomizeException(CustomizeErrorCode.REGISTER_ACCOUNT_EXIST);
        }
    }

    public UserModel getUserByToken(String token) { 
        return userRepository.findByToken(token);
    }
}
