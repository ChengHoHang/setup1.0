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
import com.chh.setup.repository.ArticleFavorRepository;
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

    @Autowired
    ArticleFavorRepository articleFavorRepository;

    /**
     * 发布新闻，并持久化到数据库
     *
     * @param articleParam
     */
    public void createOrUpdate(ArticleParam articleParam) {
        if (!ArticleTypeEnum.isExist(articleParam.getType())) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_NOT_EXIST);
        }
        UserEntity user = userRepository.findById(articleParam.getCreator()).orElse(null);
        if (user == null) {
            throw new CustomizeException(CustomizeErrorCode.USER_NOT_EXIST);
        }
        ArticleEntity article;
        if (articleParam.getId() == null) { // 插入
            article = new ArticleEntity();
            article.setGmtCreated(System.currentTimeMillis());
        } else {  // 更新
            article = getEntityById(articleParam.getId());
            if (article == null) {
                throw new CustomizeException(CustomizeErrorCode.ARTICLE_NOT_FOUND);
            }
        }
        BeanUtils.copyProperties(articleParam, article);
        article.setType(ArticleTypeEnum.getType(articleParam.getType()));
        article.setGmtModified(System.currentTimeMillis());
        articleRepository.save(article);
    }

    /**
     * 将ArticleEntity转换成ArticleDto类型
     * @param article
     * @return
     */
    public ArticleDto preConvert(ArticleEntity article) {
        UserEntity user = userRepository.findById(article.getCreator()).get();
        ArticleDto articleDto = new ArticleDto();
        BeanUtils.copyProperties(article, articleDto);
        articleDto.setGmtCreated(DateUtils.timestamp2Date(article.getGmtCreated(), "yyyy-MM-dd HH:mm"));
        articleDto.setGmtModified(DateUtils.timestamp2Date(article.getGmtModified(), "yyyy-MM-dd HH:mm"));
        articleDto.setType(ArticleTypeEnum.getName(article.getType()));
        articleDto.setCreator(user);
        return articleDto;
    }

    /**
     * 调用getPageByType方法取出相应类型的文章列表，并作预处理后封装至ArticleDto
     * @param page
     * @param size
     * @param type
     * @return
     */
    public PagesDto<ArticleDto> listByType(Integer page, Integer size, Integer type) {
        if (!ArticleTypeEnum.isExist(type)) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_NOT_EXIST);
        }
        List<ArticleEntity> articles = getPageByType(page - 1, size, type);
        List<ArticleDto> articleDtos = new ArrayList<>();
        for (ArticleEntity article : articles) {
            ArticleDto articleDto = preConvert(article);
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
    
    public ArticleDto getDtoById(Integer id) {
        ArticleEntity article = getEntityById(id);
        if (article == null) {
            throw new CustomizeException(CustomizeErrorCode.ARTICLE_NOT_FOUND);
        }
        return preConvert(article);
    }

    public ArticleEntity getEntityById(Integer id) {
        return articleRepository.findById(id).orElse(null);
    }

    /**
     * 增加对应id文章的阅读数
     * @param articleId
     */
    @Transactional
    public void incViewCount(Integer articleId) {
        articleRepository.incViewCount(articleId, 1);
    }
}
