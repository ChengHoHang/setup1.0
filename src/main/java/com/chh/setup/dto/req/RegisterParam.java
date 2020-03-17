package com.chh.setup.dto.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author chh
 * @date 2020/3/14 17:11
 */
@Data
public class RegisterParam {

    @NotBlank(message = "登陆账号不能为空")
    private String account;

    @NotBlank(message = "密码不能为空")
    private String passWord;
    
    @NotBlank(message = "昵称不能为空")
    private String name;
}
