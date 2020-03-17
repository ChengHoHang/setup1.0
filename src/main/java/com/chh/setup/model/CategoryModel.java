package com.chh.setup.model;

import lombok.Data;

import javax.persistence.*;

/**
 * @author chh
 * @date 2020/3/14 20:30
 */
@Data
@Entity
@Table(name = "category")
public class CategoryModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String type;
}
