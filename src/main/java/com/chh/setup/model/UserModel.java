package com.chh.setup.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "user")
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @JsonIgnore
    private String account;
    private String name;
    @JsonIgnore
    private String password;
    private String token;
    private Date createTime;
    private Date updateTime;
}
