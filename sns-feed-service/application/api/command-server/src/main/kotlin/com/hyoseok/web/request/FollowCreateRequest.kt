package com.hyoseok.web.request

import javax.validation.constraints.Positive

data class FollowCreateRequest(
    @field:Positive(message = "followerId는 0보다 큰 값을 입력하세요")
    val followerId: Long,

    @field:Positive(message = "followeeId는 0보다 큰 값을 입력하세요")
    val followeeId: Long,
)
