package com.chh.setup.service;

import com.chh.setup.dto.ArticleDto;
import com.chh.setup.dto.PagesDto;
import com.chh.setup.dto.UserRecordDto;
import com.chh.setup.entity.UserEntity;
import com.chh.setup.myutils.DateUtils;
import com.chh.setup.repository.ArticleRepository;
import com.chh.setup.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 用户操作记录服务层
 * @author chh
 * @date 2020/1/20 16:19
 */
@Service
public class UserRecordService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ArticleRepository articleRepository;
    
    @Value("${page.size}")
    Integer pageSize;
    
    public Optional<UserEntity> findById(int userId) {
        return userRepository.findById(userId);
    }

    /**
     * 用户个人资料页面分页展示
     * @param user
     * @param page
     * @return
     */
    public UserRecordDto<ArticleDto> listArticle(UserEntity user, Integer page) {
        UserRecordDto<ArticleDto> userRecordDto = new UserRecordDto<>();
        Sort sort = Sort.by(Sort.Direction.DESC, "gmtModified");
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, sort);
        List<ArticleDto> articles = articleRepository
                .findAllByCreator(user.getId(), pageRequest)
                .getContent()
                .stream().map(article -> {
                    ArticleDto articleDto = new ArticleDto();
                    BeanUtils.copyProperties(article, articleDto, "description", "user");
                    articleDto.setUser(user);
                    articleDto.setGmtCreated(DateUtils.timestamp2Date(article.getGmtCreated(), "yyyy-MM-dd HH:mm"));
                    articleDto.setGmtModified(DateUtils.timestamp2Date(article.getGmtModified(), "yyyy-MM-dd HH:mm"));
                    return articleDto;
                }).collect(Collectors.toList());
        PagesDto<ArticleDto> pagesDto = new PagesDto<>();
        pagesDto.setData(articles);
        pagesDto.setTotalPage(getCountByCreator(user.getId()));
        userRecordDto.setData(pagesDto);
        userRecordDto.setUser(user);
        return userRecordDto;
    }

    public Long getCountByCreator(Integer creator) {
        Long count = articleRepository.countByCreator(creator);
        return (long) Math.ceil((double) count / pageSize);
    }
}
