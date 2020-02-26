package com.chh.setup.controller;

import com.chh.setup.dto.ResultDto;
import com.chh.setup.entity.UserEntity;
import com.chh.setup.exception.CustomizeErrorCode;
import com.chh.setup.exception.CustomizeException;
import com.chh.setup.repository.NoticeRepository;
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
public class NotificationController {

    @Autowired
    NoticeService noticeService;

    @Autowired
    NoticeRepository noticeRepository;

    @GetMapping({"/my-notice/unread", "/my-notice/all"})

    public String notice() {
        return "/notice.html";
    }

    @GetMapping("/notice/{userId}/{type}")
    @ResponseBody
    public Object getMyNotification(@PathVariable(name = "userId") Integer userId,
                                    @PathVariable(name = "type") String type,
                                    @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                    HttpServletRequest request) {
        UserEntity user = (UserEntity) request.getSession().getAttribute("user");
        if (user == null) {
            throw new CustomizeException(CustomizeErrorCode.USER_LOG_OUT);
        }
        if (!user.getId().equals(userId)) {
            throw new CustomizeException(CustomizeErrorCode.OPERATE_PERMISSION_DENY);
        }
        if (type.equals("all")) {
            return ResultDto.okOf(noticeService.getAllNotice(userId, page));
        } else if (type.equals("unread")) {
            return ResultDto.okOf(noticeService.getUnreadNotice(userId, page));
        } else {
            throw new CustomizeException(CustomizeErrorCode.PARAM_ERROR);
        }
    }

    @GetMapping("/notice/{id}")
    public String redirectToArticle(@PathVariable(name = "id") Integer id,
                                    HttpServletRequest request, @RequestParam("articleId") Integer articleId) {
        UserEntity user = (UserEntity) request.getSession().getAttribute("user");
        if (user == null) {
            throw new CustomizeException(CustomizeErrorCode.USER_LOG_OUT);
        }
        noticeService.readNotice(id, user.getId());
        return "redirect:/a/" + articleId;
    }

    @GetMapping("/unreadCount")
    @ResponseBody
    public Object getUnreadCount(HttpServletRequest request) {
        UserEntity user = (UserEntity) request.getSession().getAttribute("user");
        if (user == null) {
            throw new CustomizeException(CustomizeErrorCode.USER_LOG_OUT);
        }
        return noticeRepository.countByReceiverAndState(user.getId(), 0);
    }
}
