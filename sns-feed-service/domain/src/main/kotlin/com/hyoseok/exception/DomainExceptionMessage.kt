package com.hyoseok.exception

import com.hyoseok.follow.entity.Follow.Companion.MAX_FOLLOWEE_LIMIT

object DomainExceptionMessage {
    const val FAIL_ADD_FOLLOWEE = "팔로잉은 최대 ${MAX_FOLLOWEE_LIMIT}명 까지 가능합니다"
}
