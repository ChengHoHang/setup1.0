package com.chh.setup.service;

import com.chh.setup.advice.exception.CustomizeErrorCode;
import com.chh.setup.advice.exception.CustomizeException;
import com.chh.setup.dto.req.Record;
import com.chh.setup.dto.res.PagesDto;
import com.chh.setup.enums.NoticeTypeEnum;
import com.chh.setup.model.NoticeModel;
import com.chh.setup.repository.ArticleRepository;
import com.chh.setup.repository.CommentRepository;
import com.chh.setup.repository.NoticeRepository;
import com.chh.setup.repository.UserRepository;
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
    private NoticeRepository noticeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ArticleRepository articleRepository;

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
            noticeRepository.save(noticeModel);
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
            noticeModels = noticeRepository.findAllByReceiverId(userId, pageRequest).getContent();
            count = countAll(userId);
        } else {
            noticeModels = noticeRepository.findAllByReceiverIdAndState(userId, 0, pageRequest).getContent();
            count = countUnread(userId);
        } 
        noticeModels.forEach(noticeModel -> {
            noticeModel.setAction(NoticeTypeEnum.getAction(noticeModel.getType()));
            noticeModel.setNotifier(userRepository.findById(noticeModel.getNotifierId()).get());
            if (noticeModel.getType() == 1 || noticeModel.getType() == 3) {
                noticeModel.setComment(commentRepository.findById(noticeModel.getParentId()).get());
                noticeModel.getComment().setArticleTitle(articleRepository.findById(noticeModel.getComment().getArticleId()).get().getTitle());
            } else if (noticeModel.getType() == 2) {
                noticeModel.setArticle(articleRepository.findById(noticeModel.getParentId()).get());
            }
        });
        PagesDto pagesDto = new PagesDto();
        pagesDto.setData(noticeModels);
        pagesDto.setTotalPage(count);
        return pagesDto;
    }

    public Long countAll(Integer receiver) {
        Long count = noticeRepository.countByReceiverId(receiver);
        return (long) Math.ceil((double) count / pageSize);
    }

    public Long countUnread(Integer receiver) {
        Long count = noticeRepository.countByReceiverIdAndState(receiver, 0);
        return (long) Math.ceil((double) count / pageSize);
    }

    @Transactional
    public void readNotice(Integer noticeId, Integer userId) {
        NoticeModel notice = noticeRepository.findById(noticeId).orElse(null);
        if (notice == null) {
            throw new CustomizeException(CustomizeErrorCode.NOTICE_NOT_FOUND);
        }
        if (!notice.getReceiverId().equals(userId)) {
            throw new CustomizeException(CustomizeErrorCode.OPERATE_PERMISSION_DENY);
        }
        notice.setState(1);
        noticeRepository.save(notice);
    }
}
