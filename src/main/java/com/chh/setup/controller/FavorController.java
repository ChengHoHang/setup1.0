package com.chh.setup.controller;

import com.chh.setup.dto.FavorParam;
import com.chh.setup.dto.ResultDto;
import com.chh.setup.dto.muilty.Record;
import com.chh.setup.entity.UserEntity;
import com.chh.setup.exception.CustomizeErrorCode;
import com.chh.setup.exception.CustomizeException;
import com.chh.setup.service.ArticleService;
import com.chh.setup.service.FavorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author chh
 * @date 2020/2/7 0:19
 */
@RestController
public class FavorController {

    @Autowired
    ArticleService articleService;

    @Autowired
    FavorService favorService;
    
    @PostMapping("/favor-confirm")
    public Object favorConfirm(@RequestBody FavorParam favorParam, HttpServletRequest request) {
        UserEntity user = (UserEntity) request.getSession().getAttribute("user");
        if (user == null) {
            throw new CustomizeException(CustomizeErrorCode.USER_LOG_OUT);
        }
        if (articleService.getEntityById(favorParam.getArticleId()) == null) {
            throw new CustomizeException(CustomizeErrorCode.ARTICLE_NOT_FOUND);
        }
        if (favorParam.getFavorState() != null) { // 更新article表点赞数，插入或更新favor表点赞记录
            favorService.createOrUpdateStates(favorParam.getFavorState(), favorParam.getArticleId(), user.getId());
        }
        List<Record> records = favorParam.getRecords();
        if (records.size() != 0) { // 更新comment表点赞数，插入或更新favor表点赞记录
            favorService.createOrUpdateStates(records, favorParam.getArticleId(), user.getId());
        }
        return ResultDto.okOf(null);
    }
}
