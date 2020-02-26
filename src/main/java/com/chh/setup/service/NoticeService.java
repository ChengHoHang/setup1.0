package com.chh.setup.service;

import com.chh.setup.dto.NoticeDto;
import com.chh.setup.dto.PagesDto;
import com.chh.setup.dto.process.Record;
import com.chh.setup.entity.NoticeEntity;
import com.chh.setup.enums.NoticeTypeEnum;
import com.chh.setup.exception.CustomizeErrorCode;
import com.chh.setup.exception.CustomizeException;
import com.chh.setup.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author chh
 * @date 2020/2/24 19:52
 */
@Service
public class NoticeService {

    @Autowired
    NoticeRepository noticeRepository;

    @Value("${noticepage.size}")
    Integer pageSize;

    /**
     * 创建通知记录
     * @param notifier 通知者
     * @param receiver 被通知者
     * @param type 类型 {@link NoticeTypeEnum }
     * @param parentId 外键id，若type为3则parentId为commentId，其余情况均为articleId
     */
    public void createNotice(Integer notifier, Integer receiver, Integer type, Integer parentId) {
        NoticeEntity noticeEntity = new NoticeEntity();
        noticeEntity.setNotifier(notifier);
        noticeEntity.setReceiver(receiver);
        noticeEntity.setType(type);
        noticeEntity.setState(0);
        noticeEntity.setParentId(parentId);
        noticeEntity.setGmtCreated(System.currentTimeMillis());
        noticeRepository.save(noticeEntity);
    }

    public void createNotice(Integer notifier, List<Record> records, Integer type) {
        for (Record record : records) {
            if (!record.getCommentator().equals(notifier) && record.getState() == 1) {
                createNotice(notifier, record.getCommentator(), type, record.getCommentId());
            }
        }
    }

    public PagesDto getAllNotice(Integer userId, Integer page) {
        PagesDto pagesDto = new PagesDto();
        Sort sort = Sort.by(Sort.Order.asc("state"), Sort.Order.desc("gmtCreated"));
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, sort);
        List<NoticeDto> noticeDtos = noticeRepository.findAllByReceiver(userId, pageRequest).getContent();
        pagesDto.setData(noticeDtos);
        pagesDto.setTotalPage(countAll(userId));
        return pagesDto;
    }

    public Long countAll(Integer receiver) {
        Long count = noticeRepository.countByReceiver(receiver);
        return (long) Math.ceil((double) count / pageSize);
    }
    
    public PagesDto getUnreadNotice(Integer userId, Integer page) {
        PagesDto pagesDto = new PagesDto();
        Sort sort = Sort.by(Sort.Direction.DESC, "gmtCreated");
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, sort);
        List<NoticeDto> noticeDtos = noticeRepository.findUnread(userId, pageRequest).getContent();
        pagesDto.setData(noticeDtos);
        pagesDto.setTotalPage(countUnread(userId));
        return pagesDto;
    }
    
    public Long countUnread(Integer receiver) {
        Long count = noticeRepository.countByReceiverAndState(receiver, 0);
        return (long) Math.ceil((double) count / pageSize);
    }

    public void readNotice(Integer noticeId, Integer userId) {
         NoticeEntity notice = noticeRepository.findById(noticeId).orElse(null);
        if (notice == null) {
            throw new CustomizeException(CustomizeErrorCode.NOTICE_NOT_FOUND);
        }
        if (!notice.getReceiver().equals(userId)) {
            throw new CustomizeException(CustomizeErrorCode.OPERATE_PERMISSION_DENY);
        }
        notice.setState(1);
        noticeRepository.save(notice);
    }
}
