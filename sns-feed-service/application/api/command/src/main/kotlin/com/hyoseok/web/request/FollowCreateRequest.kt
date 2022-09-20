package com.hyoseok.web.request

import com.hyoseok.service.dto.FollowCreateDto
import javax.validation.constraints.Positive

data class FollowCreateRequest(
    @field:Positive(message = "followerId는 0보다 큰 값을 입력하세요")
    val followerId: Long,

    @field:Positive(message = "followeeId는 0보다 큰 값을 입력하세요")
    val followeeId: Long,
) {
    fun toServiceDto() = FollowCreateDto(followerId = followerId, followeeId = followeeId)
}
