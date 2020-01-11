package com.chh.setup.service;

import com.chh.setup.Repository.ArticleRepository;
import com.chh.setup.Repository.UserRepository;
import com.chh.setup.dto.ArticleDto;
import com.chh.setup.dto.ArticleFEDto;
import com.chh.setup.entity.ArticleEntity;
import com.chh.setup.entity.UserEntity;
import com.chh.setup.enums.ArticleTypeEnum;
import com.chh.setup.myutils.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chh
 * @date 2020/1/10 21:12
 */
@Service
public class ArticleService {

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    UserRepository userRepository;

    public void createOrUpdate(ArticleFEDto articleFEDto) {
        ArticleEntity articleEntity = new ArticleEntity();
        BeanUtils.copyProperties(articleFEDto, articleEntity);
        articleEntity.setType(ArticleTypeEnum.getType(articleFEDto.getType()));
        articleEntity.setGmtCreated(System.currentTimeMillis());
        articleEntity.setGmtModified(System.currentTimeMillis());
        articleRepository.save(articleEntity);
    }


    public List<ArticleDto> listArticle() {
        List<ArticleEntity> articles = articleRepository.findAllByOrderByGmtModifiedDesc();
        List<ArticleDto> articleDtos = new ArrayList<>();
        for (ArticleEntity article : articles) {
            UserEntity user = userRepository.findById(article.getCreator()).get();
            ArticleDto articleDto = new ArticleDto();
            BeanUtils.copyProperties(article, articleDto);
            articleDto.setGmtCreated(DateUtils.timestamp2Date(article.getGmtCreated(), "yyyy-MM-dd HH:mm"));
            articleDto.setGmtModified(DateUtils.timestamp2Date(article.getGmtModified(), "yyyy-MM-dd HH:mm"));
            articleDto.setUser(user);
            articleDtos.add(articleDto);
        }
        return articleDtos;
    }
}
