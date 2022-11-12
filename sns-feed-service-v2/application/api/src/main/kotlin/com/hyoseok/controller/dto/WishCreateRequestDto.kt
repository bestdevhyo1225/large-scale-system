package com.hyoseok.controller.dto

import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.constraints.Positive

data class WishCreateRequestDto(
    @field:Positive(message = "postId는 0보다 큰 값을 입력해야 합니다")
    @field:Schema(description = "게시글 번호", example = "1", required = true)
    val postId: Long,

    @field:Positive(message = "memberId는 0보다 큰 값을 입력해야 합니다")
    @field:Schema(description = "회원 번호", example = "2", required = true)
    val memberId: Long,
)
