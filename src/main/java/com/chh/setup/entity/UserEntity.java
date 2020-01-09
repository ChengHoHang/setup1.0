package com.chh.setup.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String account;
    private String name;
    private String passWord;
    private String token;
    private Long gmtCreated;
    private Long gmtModified;
}
