package com.chh.setup.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author chh
 * @date 2020/2/24 15:07
 */
@Data
@Entity
@Table(name = "notice")
public class NoticeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer notifier;
    private Integer receiver;
    private Integer type;
    private Integer parentId;
    private Integer state;
    private Long gmtCreated;
}
