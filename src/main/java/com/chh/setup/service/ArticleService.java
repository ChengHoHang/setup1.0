package com.chh.setup.service;

import com.chh.setup.Repository.ArticleRepository;
import com.chh.setup.dto.ArticleFEDto;
import com.chh.setup.entity.ArticleEntity;
import com.chh.setup.enums.ArticleTypeEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author chh
 * @date 2020/1/10 21:12
 */
@Service
public class ArticleService {
    
    @Autowired
    private ArticleRepository articleRepository;
    
    public void createOrUpdate(ArticleFEDto articleFEDto) {
        ArticleEntity articleEntity = new ArticleEntity();
        BeanUtils.copyProperties(articleFEDto, articleEntity);
        articleEntity.setType(ArticleTypeEnum.getType(articleFEDto.getType()));
        articleEntity.setGmtCreated(System.currentTimeMillis());
        articleEntity.setGmtModified(System.currentTimeMillis());
        articleRepository.save(articleEntity);
    }
}
