package com.hyoseok.controller.dto

import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.constraints.Positive

data class FollowCreateRequestDto(
    @field:Positive(message = "followerId는 0보다 큰 값을 입력하세요")
    @field:Schema(description = "팔로우 회원번호", example = "1", required = true)
    val followerId: Long,

    @field:Positive(message = "followeeId는 0보다 큰 값을 입력하세요")
    @field:Schema(description = "팔로이 회원번호", example = "2", required = true)
    val followeeId: Long,
)
