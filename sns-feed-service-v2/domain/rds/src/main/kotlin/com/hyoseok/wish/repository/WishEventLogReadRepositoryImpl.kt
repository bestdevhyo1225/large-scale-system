package com.hyoseok.wish.repository

import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = true)
class WishEventLogReadRepositoryImpl {

    object ErrorMessage {
        const val NOT_FOUND_WISH_EVENT_LOG = "좋아요 이벤트 로그를 찾을 수 없습니다"
    }
}
