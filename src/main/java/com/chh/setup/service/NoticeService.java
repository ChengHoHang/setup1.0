package com.chh.setup.service;

import com.chh.setup.advice.exception.CustomizeErrorCode;
import com.chh.setup.advice.exception.CustomizeException;
import com.chh.setup.dto.req.Record;
import com.chh.setup.dto.res.PagesDto;
import com.chh.setup.enums.NoticeTypeEnum;
import com.chh.setup.model.NoticeModel;
import com.chh.setup.dao.ArticleDao;
import com.chh.setup.dao.CommentDao;
import com.chh.setup.dao.NoticeDao;
import com.chh.setup.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author chh
 * @date 2020/2/24 19:52
 */
@Service
public class NoticeService {

    @Autowired
    private NoticeDao noticeDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private ArticleDao articleDao;

    @Value("${noticepage.size}")
    private Integer pageSize;

    /**
     * 创建通知记录
     *
     * @param notifierId 通知者
     * @param receiverId 被通知者
     * @param type     类型 {@link NoticeTypeEnum }
     * @param parentId 外键id，若type为3则parentId为commentId，其余情况均为articleId
     */
    public void createNotice(Integer notifierId, Integer receiverId, Integer type, Integer parentId) {
        NoticeModel noticeModel = new NoticeModel();
        noticeModel.setCreateTime(new Date());
        noticeModel.setNotifierId(notifierId);
        noticeModel.setReceiverId(receiverId);
        noticeModel.setType(type);
        noticeModel.setState(0);
        noticeModel.setParentId(parentId);
        try {
            noticeDao.save(noticeModel);
        } catch (DataIntegrityViolationException ignored) { }
    }

    public void createNotice(Integer notifierId, List<Record> records, Integer type) {
        for (Record record : records) {
            if (!record.getCommentatorId().equals(notifierId) && record.getState() == 1) {
                createNotice(notifierId, record.getCommentatorId(), type, record.getCommentId());
            }
        }
    }

    public PagesDto getANoticeDto(Integer userId, Integer page, String type) {
        Sort sort = Sort.by(Sort.Order.asc("state"), Sort.Order.desc("createTime"));
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, sort);
        List<NoticeModel> noticeModels;
        Long count;
        if ("all".equals(type)) {
            noticeModels = noticeDao.findAllByReceiverId(userId, pageRequest).getContent();
            count = countAll(userId);
        } else {
            noticeModels = noticeDao.findAllByReceiverIdAndState(userId, 0, pageRequest).getContent();
            count = countUnread(userId);
        } 
        noticeModels.forEach(noticeModel -> {
            noticeModel.setAction(NoticeTypeEnum.getAction(noticeModel.getType()));
            noticeModel.setNotifier(userDao.findById(noticeModel.getNotifierId()).get());
            if (noticeModel.getType() == 1 || noticeModel.getType() == 3) {
                noticeModel.setComment(commentDao.findById(noticeModel.getParentId()).get());
                noticeModel.getComment().setArticleTitle(articleDao.findById(noticeModel.getComment().getArticleId()).get().getTitle());
            } else if (noticeModel.getType() == 2) {
                noticeModel.setArticle(articleDao.findById(noticeModel.getParentId()).get());
            }
        });
        PagesDto pagesDto = new PagesDto();
        pagesDto.setData(noticeModels);
        pagesDto.setTotalPage(count);
        return pagesDto;
    }

    public Long countAll(Integer receiver) {
        Long count = noticeDao.countByReceiverId(receiver);
        return (long) Math.ceil((double) count / pageSize);
    }

    public Long countUnread(Integer receiver) {
        Long count = noticeDao.countByReceiverIdAndState(receiver, 0);
        return (long) Math.ceil((double) count / pageSize);
    }

    @Transactional
    public void readNotice(Integer noticeId, Integer userId) {
        NoticeModel notice = noticeDao.findById(noticeId).orElse(null);
        if (notice == null) {
            throw new CustomizeException(CustomizeErrorCode.NOTICE_NOT_FOUND);
        }
        if (!notice.getReceiverId().equals(userId)) {
            throw new CustomizeException(CustomizeErrorCode.OPERATE_PERMISSION_DENY);
        }
        notice.setState(1);
        noticeDao.save(notice);
    }
}
