package com.chh.setup.service;

import com.chh.setup.dto.ArticleDto;
import com.chh.setup.dto.ArticleParam;
import com.chh.setup.dto.PagesDto;
import com.chh.setup.entity.ArticleEntity;
import com.chh.setup.entity.UserEntity;
import com.chh.setup.enums.ArticleTypeEnum;
import com.chh.setup.exception.CustomizeErrorCode;
import com.chh.setup.exception.CustomizeException;
import com.chh.setup.myutils.PageUtils;
import com.chh.setup.repository.ArticleRepository;
import com.chh.setup.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        if (!ArticleTypeEnum.isExist(articleParam.getType())) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_NOT_EXIST);
        }
        String[] tagSplit = StringUtils.split(articleParam.getTag(), ",");
        if (TagService.isInvalid(tagSplit)) {
            throw new CustomizeException(CustomizeErrorCode.TAG_NOT_EXIST);
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
        BeanUtils.copyProperties(articleParam, article, "type", "gmtModified", "tag");
        article.setType(ArticleTypeEnum.getType(articleParam.getType()));
        article.setGmtModified(System.currentTimeMillis());
        Map<String, String> remarkMap = TagService.getRemarkMap();
        article.setTag(Arrays.stream(tagSplit).map(remarkMap::get).collect(Collectors.joining(",")));
        articleRepository.save(article);
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
        PageRequest pageRequest = PageUtils.getDefaultPageRequest(page, size, "gmtModified");
        List<ArticleDto> articleDtos;
        if (type != 0) {
            articleDtos = articleRepository.getAllDtoByType(type, pageRequest).getContent();
        } else {
            articleDtos = articleRepository.getAllDto(pageRequest).getContent();
        }
        articleDtos.forEach(articleDto -> {
            articleDto.setDescription(StringUtils.truncate(articleDto.getDescription(), 150) + ".....");
            articleDto.setTags(Arrays.stream(articleDto.getTags()).limit(2).map(tag -> TagService.getIdMap().get(tag)).toArray(String[]::new));
        });
        PagesDto<ArticleDto> pagesDto = new PagesDto<>();
        pagesDto.setData(articleDtos);
        pagesDto.setTotalPage(getCountByType(type, size));
        return pagesDto;
    }

    /**
     * 获取各类文章总页数
     * @param type
     * @param size
     * @return
     */
    public Long getCountByType(Integer type, Integer size) {
        long count = type == 0 ? articleRepository.count() : articleRepository.countByType(type);
        return (long) Math.ceil((double) count / size);
    }

    /**
     * 将ArticleEntity转换成ArticleDto类型
     * @param id articleId
     * @return
     */
    public ArticleDto getDtoById(Integer id) {
        ArticleDto articleDto = articleRepository.findDtoById(id);
        if (articleDto == null) {  // 当然 也可能是作者的数据丢失了；不过小型网站就不做的这么细了
            throw new CustomizeException(CustomizeErrorCode.ARTICLE_NOT_FOUND);
        }
        return articleDto;
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

    
    public List<Object[]> getRelatedArticle(Integer id, String[] tags) {
        if (tags == null) {
            return new ArrayList<>();
        }
        return articleRepository.getRelatedArticleById(id, StringUtils.join(tags, '|'), 0, 15);
    }
}
