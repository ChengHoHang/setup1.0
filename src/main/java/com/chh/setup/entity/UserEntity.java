package com.chh.setup.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @JsonIgnore
    private String account;
    private String name;
    @JsonIgnore
    private String passWord;
    private String token;
    private Long gmtCreated;
    private Long gmtModified;
}
