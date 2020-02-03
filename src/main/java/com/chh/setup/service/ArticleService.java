package com.chh.setup.service;

import com.chh.setup.dto.ArticleDto;
import com.chh.setup.dto.ArticleParam;
import com.chh.setup.dto.PagesDto;
import com.chh.setup.entity.ArticleEntity;
import com.chh.setup.entity.UserEntity;
import com.chh.setup.enums.ArticleTypeEnum;
import com.chh.setup.exception.CustomizeErrorCode;
import com.chh.setup.exception.CustomizeException;
import com.chh.setup.myutils.DateUtils;
import com.chh.setup.repository.ArticleRepository;
import com.chh.setup.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * 发布新闻，并持久化到数据库
     *
     * @param articleParam
     */
    public void createOrUpdate(ArticleParam articleParam) {
        ArticleEntity articleEntity = new ArticleEntity();
        BeanUtils.copyProperties(articleParam, articleEntity);
        articleEntity.setType(ArticleTypeEnum.getType(articleParam.getType()));
        articleEntity.setGmtCreated(System.currentTimeMillis());
        articleEntity.setGmtModified(System.currentTimeMillis());
        articleRepository.save(articleEntity);
    }

    /**
     * 将ArticleEntity转换成ArticleDto类型
     * @param article
     * @return
     */
    public ArticleDto convertToDto(ArticleEntity article) {
        UserEntity user = userRepository.findById(article.getCreator()).get();
        ArticleDto articleDto = new ArticleDto();
        BeanUtils.copyProperties(article, articleDto);
        articleDto.setGmtCreated(DateUtils.timestamp2Date(article.getGmtCreated(), "yyyy-MM-dd HH:mm"));
        articleDto.setGmtModified(DateUtils.timestamp2Date(article.getGmtModified(), "yyyy-MM-dd HH:mm"));
        articleDto.setType(ArticleTypeEnum.getName(article.getType()));
        articleDto.setUser(user);
        return articleDto;
    }
    
    /**
     * 调用getPageByType方法取出相应类型的文章列表，并作预处理后封装至ArticleDto
     */
    public PagesDto<ArticleDto> listByType(Integer page, Integer size, Integer type) {
        List<ArticleEntity> articles = getPageByType(page - 1, size, type);
        List<ArticleDto> articleDtos = new ArrayList<>();
        for (ArticleEntity article : articles) {
            ArticleDto articleDto = convertToDto(article);
            articleDto.setDescription(StringUtils.truncate(articleDto.getDescription(), 150) + ".....");
            articleDtos.add(articleDto);
        }
        PagesDto<ArticleDto> pagesDto = new PagesDto<>();
        pagesDto.setData(articleDtos);
        pagesDto.setTotalPage(getCountByType(type, size));
        return pagesDto;
    }

    /**
     * 默认以修改时间降序排列
     *
     * @param page 第page页，接口的page起始为0
     * @param size 每页文章数量
     * @param type 当前指定浏览文章类型，详情看 com.chh.setup.enums.ArticleTypeEnum
     * @return
     */
    public List<ArticleEntity> getPageByType(Integer page, Integer size, Integer type) {
        Sort sort = Sort.by(Sort.Direction.DESC, "gmtModified");
        return getPageByType(page, size, type, sort);
    }

    public List<ArticleEntity> getPageByType(Integer page, Integer size, Integer type, Sort sort) {
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        List<ArticleEntity> articles;
        if (type == 0) {
            articles = articleRepository.findAll(pageRequest).getContent();
        } else {
            articles = articleRepository.findAllByType(type, pageRequest).getContent();
        }
        return articles;
    }

    /**
     * 获取各类文章总页数
     *
     * @param type
     * @param size
     * @return
     */
    public Long getCountByType(Integer type, Integer size) {
        long count = type == 0 ? articleRepository.count() : articleRepository.countByType(type);
        return (long) Math.ceil((double) count / size);
    }
    
    public ArticleDto getArticleById(Integer id) {
        ArticleEntity article = articleRepository.findById(id).orElse(null);
        if (article == null) {
            throw new CustomizeException(CustomizeErrorCode.ARTICLE_NOT_FOUND);
        }
        return convertToDto(article);
    }

    /**
     * 增加对应id文章的阅读数
     * @param id
     */
    @Transactional
    public void incViewCount(Integer id) {
        articleRepository.incViewCount(id, 1);
    }
}
