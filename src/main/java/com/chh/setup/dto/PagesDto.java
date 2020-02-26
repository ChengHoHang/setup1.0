package com.chh.setup.dto;

import com.chh.setup.dto.process.PageSuperDto;
import lombok.Data;

import java.util.List;

/**
 * 供前端分页组件使用
 * @author chh
 * @date 2020/1/13 21:22
 */
@Data
public class PagesDto {
    
    List<? extends PageSuperDto> data;  
    Long totalPage;  //各类记录总页数
}
