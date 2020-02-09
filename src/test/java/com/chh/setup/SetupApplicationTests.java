package com.chh.setup;

import com.chh.setup.entity.ArticleEntity;
import com.chh.setup.entity.CommentFavorEntity;
import com.chh.setup.entity.UserEntity;
import com.chh.setup.enums.ArticleTypeEnum;
import com.chh.setup.repository.ArticleRepository;
import com.chh.setup.repository.CommentFavorRepository;
import com.chh.setup.repository.CommentRepository;
import com.chh.setup.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class SetupApplicationTests {

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    UserRepository userRepository;
    
    @Autowired
    CommentFavorRepository commentFavorRepository;

    @Autowired
    CommentRepository commentRepository;

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

    @Test
    public void test4() {
        String name = ArticleTypeEnum.getName(1);
        System.out.println(name);
    }

    @Test
    public void test5() {
        Integer type = 1;
        CommentFavorEntity favor = new CommentFavorEntity();
        favor.setUserId(4);
        commentFavorRepository.save(favor);
    }

    @Test
    public void test6() {
        CommentFavorEntity favor = commentFavorRepository.findById(1).get();
        System.out.println(favor);
    }

    @Test
    public void test7() {
        List<CommentFavorEntity> record = commentFavorRepository.getFavourComments(Arrays.asList(2, 3), 4, 35);
        System.out.println(record);
    }

    @Test
    public void test8() {
        ArrayList<Object> list = new ArrayList<>();
        test9(list);
        System.out.println(list);
    }
    
    public void test9(ArrayList<Object> list) {
        list.add(1);
    }
    
    @Test
    public void test9() {
        UserEntity userEntity2 = new UserEntity();
        userEntity2.setAccount("ab123456");
        userEntity2.setId(7);
        userEntity2.setPassWord("123456");
        List<UserEntity> list = new ArrayList<>();
        list.add(userEntity2);
        userRepository.saveAll(list);
    }

    @Test
    public void test10() {
        commentRepository.incLikeCount(2, 1);
    }
}
