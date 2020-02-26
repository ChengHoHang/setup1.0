package com.chh.setup.repository;

import com.chh.setup.dto.NoticeDto;
import com.chh.setup.entity.NoticeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NoticeRepository extends JpaRepository<NoticeEntity, Integer> {

    @Query("select new com.chh.setup.dto.NoticeDto(n, a, c, u) from NoticeEntity n " +
            "left join ArticleEntity a on a.id = n.parentId and (n.type = 1 or n.type = 2) " +
            "left join CommentEntity c on c.id = n.parentId and n.type = 3 " +
            "join UserEntity u on u.id = n.notifier and n.receiver = :receiver")
    Page<NoticeDto> findAllByReceiver(@Param("receiver") Integer receiver, Pageable pageable);

    Long countByReceiver(Integer receiver);

    @Query("select new com.chh.setup.dto.NoticeDto(n, a, c, u) from NoticeEntity n " +
            "left join ArticleEntity a on a.id = n.parentId and (n.type = 1 or n.type = 2) " +
            "left join CommentEntity c on c.id = n.parentId and n.type = 3 " +
            "join UserEntity u on u.id = n.notifier and n.receiver = :receiver and n.state = 0") 
    Page<NoticeDto> findUnread(@Param("receiver")Integer receiver, Pageable pageable);

    Long countByReceiverAndState(Integer receiver, Integer state);
}
