package com.chh.setup.dto.res;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author chh
 * @date 2020/3/24 19:27
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class SearchDto extends PagesDto {

    private Long totalCount;
    private List<String> otherKeyword;
    
}
