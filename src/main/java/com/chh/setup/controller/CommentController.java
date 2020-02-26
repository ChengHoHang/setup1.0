package com.chh.setup.controller;

import com.chh.setup.dto.CommentParam;
import com.chh.setup.dto.ResultDto;
import com.chh.setup.entity.UserEntity;
import com.chh.setup.enums.NoticeTypeEnum;
import com.chh.setup.exception.CustomizeErrorCode;
import com.chh.setup.exception.CustomizeException;
import com.chh.setup.service.CommentService;
import com.chh.setup.service.NoticeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author chh
 * @date 2020/2/4 17:13
 */
@RestController
public class CommentController {

    @Autowired
    CommentService commentService;

    @Autowired
    NoticeService noticeService;

    @PostMapping("/publish/comment")
    public Object publishComment(@RequestBody CommentParam commentParam,
                                 HttpServletRequest request) {
        UserEntity user = (UserEntity) request.getSession().getAttribute("user");
        if (user == null || !commentParam.getCommentator().equals(user.getId())) {
            throw new CustomizeException(CustomizeErrorCode.USER_LOG_OUT);
        }
        if (StringUtils.isBlank(commentParam.getContent()) || "".equals(StringUtils.trim(commentParam.getContent()))) {
            throw new CustomizeException(CustomizeErrorCode.BLANK_CONTENT);
        }
        if (commentParam.getCommentator() == null || commentParam.getCommentator() <= 0 || 
                commentParam.getAuthor() == null || commentParam.getAuthor() <= 0) {
            throw new CustomizeException(CustomizeErrorCode.USER_NOT_EXIST);
        }
        if (commentParam.getArticleId() == null || commentParam.getArticleId() <= 0) {
            throw new CustomizeException(CustomizeErrorCode.ARTICLE_NOT_FOUND);
        }
        commentService.createOrUpdate(commentParam);
        if (!user.getId().equals(commentParam.getCommentator())) {
            noticeService.createNotice(commentParam.getCommentator(), commentParam.getAuthor(),
                    NoticeTypeEnum.REPLAY_ARTICLE.getType(), commentParam.getArticleId());
        }
        return ResultDto.okOf(null);
    }

}
