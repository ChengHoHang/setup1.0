package com.chh.setup.dao;

import com.chh.setup.model.NoticeModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeDao extends JpaRepository<NoticeModel, Integer> {
    
    Page<NoticeModel> findAllByReceiverId(Integer receiverId, Pageable pageable);
    
    Long countByReceiverId(Integer receiverId);

    Page<NoticeModel> findAllByReceiverIdAndState(Integer receiverId, Integer state, Pageable pageable);
    
    Long countByReceiverIdAndState(Integer receiverId, Integer state);
}
