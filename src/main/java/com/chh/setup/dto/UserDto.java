package com.chh.setup.dto;

import com.chh.setup.entity.UserEntity;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * @author chh
 * @date 2020/2/11 16:08
 */
@Data
public class UserDto {
    private Integer id;
    private String name;
    private Long gmtModified;

    public UserDto() { }

    public UserDto(UserEntity userEntity) {
        BeanUtils.copyProperties(userEntity, this);
    }
}
