package com.chh.setup.repository;

import com.chh.setup.model.NoticeModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<NoticeModel, Integer> {

//    @Query("select new com.chh.setup.dto.res.NoticeDto(n, a, c, u) from NoticeModel n " +
//            "left join ArticleModel a on a.id = n.parentId and (n.type = 1 or n.type = 2) " +
//            "left join CommentModel c on c.id = n.parentId and n.type = 3 " +
//            "join UserModel u on u.id = n.notifierId and n.receiverId = :receiver")
//    Page<NoticeDto> findAllByReceiver(@Param("receiver") Integer receiver, Pageable pageable);

    Page<NoticeModel> findAllByReceiverId(Integer receiverId, Pageable pageable);
    
    
    Long countByReceiverId(Integer receiverId);

//    @Query("select new com.chh.setup.dto.res.NoticeDto(n, a, c, u) from NoticeModel n " +
//            "left join ArticleModel a on a.id = n.parentId and (n.type = 1 or n.type = 2) " +
//            "left join CommentModel c on c.id = n.parentId and n.type = 3 " +
//            "join UserModel u on u.id = n.notifierId and n.receiverId = :receiver and n.state = 0") 
//    Page<NoticeDto> findUnread(@Param("receiver")Integer receiver, Pageable pageable);

    Page<NoticeModel> findAllByReceiverIdAndState(Integer receiverId, Integer state, Pageable pageable);
    
    Long countByReceiverIdAndState(Integer receiverId, Integer state);
}
