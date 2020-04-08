package com.chh.setup.controller;

import com.alibaba.fastjson.JSON;
import com.chh.setup.dto.req.LogRow;
import com.chh.setup.enums.EventTypeEnum;
import com.chh.setup.model.UserModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author chh
 * @date 2020/4/6 17:26
 */
@Controller
public class LogController {

    private static final Logger logger = LoggerFactory.getLogger("recordLogger");
    
    @GetMapping("/log")
    public void recordLog(
            @RequestParam(value = "userId") Integer userId,
            @RequestParam(value = "event") String event,
            @RequestParam(value = "articleId") Integer articleId,
            HttpServletRequest request, HttpServletResponse response) {
        UserModel user = (UserModel) request.getSession().getAttribute("user");
        if (user == null || !user.getId().equals(userId)) {
            return;
        }
        LogRow logRow = new LogRow();
        logRow.setUserId(userId);
        logRow.setArticleId(articleId);
        logRow.setCreateTime(new Date());
        logRow.setEventType(EventTypeEnum.valueOf(event));
        String s = JSON.toJSONString(logRow);
        logger.info(s + ",");
    }
    
}
