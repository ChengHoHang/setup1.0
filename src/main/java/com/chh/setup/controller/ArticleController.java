package com.chh.setup.controller;

import com.alibaba.fastjson.JSON;
import com.chh.setup.advice.exception.CustomizeErrorCode;
import com.chh.setup.advice.exception.CustomizeException;
import com.chh.setup.advice.exception.JumpExcetion;
import com.chh.setup.dto.req.CommentParam;
import com.chh.setup.dto.req.FavorParam;
import com.chh.setup.dto.req.LogRow;
import com.chh.setup.dto.req.Record;
import com.chh.setup.dto.res.ResultDto;
import com.chh.setup.enums.EventTypeEnum;
import com.chh.setup.enums.FavorStateEnum;
import com.chh.setup.enums.NoticeTypeEnum;
import com.chh.setup.model.ArticleModel;
import com.chh.setup.model.UserModel;
import com.chh.setup.myutils.NetUtils;
import com.chh.setup.service.ArticleService;
import com.chh.setup.service.CommentService;
import com.chh.setup.service.FavorService;
import com.chh.setup.service.NoticeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author chh
 * @date 2020/1/11 17:42
 */
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private FavorService favorService;

    @Autowired
    private NoticeService noticeService;
    
    @Autowired
    private HttpServletRequest request;

    private static final Logger logger = LoggerFactory.getLogger("recordLogger");

    @GetMapping("/{id}")
    public Object getArticleById(@PathVariable("id") Integer articleId) throws IOException {
        ArticleModel articleModel = articleService.getEntityById(articleId);
        if (articleModel == null) {
            throw new JumpExcetion(CustomizeErrorCode.ARTICLE_NOT_FOUND);
        }
        articleService.incViewCount(articleId);
        articleModel = articleService.getDtoModel(articleModel, request);
        return ResultDto.okOf(articleModel);
    }

    @PostMapping("/publish/comment")
    public Object publishComment(@Valid @RequestBody CommentParam commentParam, BindingResult bindingResult) {
        UserModel user = (UserModel) request.getSession().getAttribute("user");
        if (user == null || !commentParam.getCommentatorId().equals(user.getId())) {
            throw new CustomizeException(CustomizeErrorCode.OPERATE_PERMISSION_DENY);
        }
        if (bindingResult.hasErrors()) {
            throw new CustomizeException(CustomizeErrorCode.PARAM_ERROR, NetUtils.processErrorMsg(bindingResult));
        }
        commentService.createCommentAndNotice(commentParam, user);
        return ResultDto.okOf(null);
    }

    @PostMapping("/favor-confirm")
    public Object favorConfirm(@RequestBody FavorParam favorParam) {
        UserModel user = (UserModel) request.getSession().getAttribute("user");
        if (user == null || favorParam.getCurrentUserId() == null) {
            throw new CustomizeException(CustomizeErrorCode.USER_LOG_OUT);
        }
        if (!user.getId().equals(favorParam.getCurrentUserId())) {
            throw new CustomizeException(CustomizeErrorCode.OPERATE_PERMISSION_DENY);
        }
        if (articleService.getEntityById(favorParam.getArticleId()) == null) {
            throw new CustomizeException(CustomizeErrorCode.ARTICLE_NOT_FOUND);
        }
        if (favorParam.getFavorState() != null) { // 更新article表点赞数，插入或更新favor表点赞记录
            if (favorParam.getFavorState() == FavorStateEnum.FAVOR.getState()) {
                logger.info(JSON.toJSONString(
                        new LogRow(user.getId(), EventTypeEnum.favor, favorParam.getArticleId(), new Date())
                        ) + ",");
            }
            favorService.createOrUpdateStates(favorParam.getFavorState(), favorParam.getArticleId(), user.getId());
            if (favorParam.getFavorState() == FavorStateEnum.FAVOR.getState() && !user.getId().equals(favorParam.getArticleAuthor())) {
                noticeService.createNotice(user.getId(), favorParam.getArticleAuthor(), NoticeTypeEnum.LIKE_ARTICLE.getType(), favorParam.getArticleId());
            }
        }
        List<Record> records = favorParam.getRecords();
        if (records.size() != 0) { // 更新comment表点赞数，插入或更新favor表点赞记录
            favorService.createOrUpdateStates(records, favorParam.getArticleId(), user.getId());
            noticeService.createNotice(user.getId(), records, NoticeTypeEnum.LIKE_COMMENT.getType());
        }
        return ResultDto.okOf(null);
    }
}
