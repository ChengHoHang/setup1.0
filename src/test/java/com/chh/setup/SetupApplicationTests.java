package com.chh.setup;

import com.chh.setup.entity.ArticleEntity;
import com.chh.setup.repository.ArticleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

@SpringBootTest
class SetupApplicationTests {

    @Autowired
    ArticleRepository articleRepository;

    @Test
    void contextLoads() {
        Sort sort = Sort.by(Sort.Direction.DESC, "gmtModified");
        PageRequest pageRequest = PageRequest.of(2, 2, sort);
        Page<ArticleEntity> pages = articleRepository.findAll(pageRequest);
        List<ArticleEntity> content = pages.getContent();
        for (ArticleEntity articleEntity : content) {
            System.out.println(articleEntity.getTitle());
        }
    }

    @Test
    public void test1() {
        Sort sort = Sort.by(Sort.Direction.DESC, "gmtModified");
        PageRequest pageRequest = PageRequest.of(0, 5, sort);
        List<ArticleEntity> contents = articleRepository.findAllByType(3, pageRequest).getContent();
        for (ArticleEntity content : contents) {
            System.out.println(content.getTitle());
        }
    }

    @Test
    public void test2() {
        int page = 0;
        int size = 5;
        int type = 2;
        Sort sort = Sort.by(Sort.Direction.DESC, "gmtModified");
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        List<ArticleEntity> contents;
        if (type == 0) {
            contents = articleRepository.findAll(pageRequest).getContent();
        } else {
            contents = articleRepository.findAllByType(type, pageRequest).getContent();
        }
        for (ArticleEntity content : contents) {
            System.out.println(content.getTitle());
        }
    }

    @Test
    public void test3() {
        long count = articleRepository.countByType(3);
        System.out.println(count);
    }
}
