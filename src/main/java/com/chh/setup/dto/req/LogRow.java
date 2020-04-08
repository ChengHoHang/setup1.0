package com.chh.setup.dto.req;

import com.alibaba.fastjson.annotation.JSONField;
import com.chh.setup.enums.EventTypeEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author chh
 * @date 2020/4/6 20:25
 */
@Data
public class LogRow implements Serializable {

    private Integer userId;
    
    private EventTypeEnum eventType;   
    
    private Integer articleId;

    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public LogRow() {
    }

    public LogRow(Integer userId, EventTypeEnum eventType, Integer articleId, Date createTime) {
        this.userId = userId;
        this.eventType = eventType;
        this.articleId = articleId;
        this.createTime = createTime;
    }
}
