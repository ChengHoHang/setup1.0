package com.chh.setup.dto;

import com.chh.setup.dto.muilty.Record;
import lombok.Data;

import java.util.List;

/**
 * @author chh
 * @date 2020/2/6 23:44
 */
@Data
public class FavorParam {
    
    private Integer articleId;
    private List<Record> records;
    private Integer favorState;   // 若无改变则接收null
}