package com.chh.setup.myutils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/**
 * @author chh
 * @date 2020/2/10 22:31
 */
public class PageUtils {

    /**
     * 获取默认分页请求，按喜欢数、修改时间字段从大到小排序
     * @param page 从1开始
     * @param pageSize 分页数量
     * @return
     */
    public static PageRequest getDefaultPageRequest(Integer page, Integer pageSize, String... properties) {
        Sort sort = Sort.by(Sort.Direction.DESC, properties);
        return PageRequest.of(page - 1, pageSize, sort);
    }
}
