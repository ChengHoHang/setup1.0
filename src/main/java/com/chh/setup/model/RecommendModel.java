package com.chh.setup.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author chh
 * @date 2020/4/8 17:29
 */
@Data
@Entity
@Table(name = "recommend")
public class RecommendModel {

    @Id
    private Integer userId;
    private String recommendIds;
}
