package com.chh.setup.controller;

import com.chh.setup.dto.res.ResultDto;
import com.chh.setup.model.UserModel;
import com.chh.setup.advice.exception.CustomizeErrorCode;
import com.chh.setup.advice.exception.CustomizeException;
import com.chh.setup.dao.NoticeDao;
import com.chh.setup.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author chh
 * @date 2020/2/24 19:51
 */
@Controller
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private NoticeDao noticeDao;

    @GetMapping("/{userId}/{type}")
    @ResponseBody
    public Object getMyNotification(@PathVariable(name = "userId") Integer userId,
                                    @PathVariable(name = "type") String type,
                                    @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                    HttpServletRequest request) {
        UserModel user = (UserModel) request.getSession().getAttribute("user");
        if (user == null) {
            throw new CustomizeException(CustomizeErrorCode.USER_LOG_OUT);
        }
        if (!user.getId().equals(userId)) {
            throw new CustomizeException(CustomizeErrorCode.OPERATE_PERMISSION_DENY);
        }
        if (!"all".equals(type) && !"unread".equals(type)) {
            throw new CustomizeException(CustomizeErrorCode.PARAM_ERROR);
        }
        return ResultDto.okOf(noticeService.getANoticeDto(userId, page, type));
    }

    @GetMapping("/{id}")
    public String redirectToArticle(@PathVariable(name = "id") Integer id,
                                    HttpServletRequest request, @RequestParam("articleId") Integer articleId) {
        UserModel user = (UserModel) request.getSession().getAttribute("user");
        if (user == null) {
            throw new CustomizeException(CustomizeErrorCode.USER_LOG_OUT);
        }
        noticeService.readNotice(id, user.getId());
        return "redirect:/a/" + articleId;
    }

    @GetMapping("/unreadCount")
    @ResponseBody
    public Object getUnreadCount(HttpServletRequest request) {
        UserModel user = (UserModel) request.getSession().getAttribute("user");
        if (user == null) {
            throw new CustomizeException(CustomizeErrorCode.USER_LOG_OUT);
        }
        return noticeDao.countByReceiverIdAndState(user.getId(), 0);
    }
}
