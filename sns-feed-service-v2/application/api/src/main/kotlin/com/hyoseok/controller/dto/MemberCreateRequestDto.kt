package com.hyoseok.controller.dto

import com.hyoseok.member.dto.MemberCreateDto
import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.constraints.NotBlank

data class MemberCreateRequestDto(
    @field:NotBlank(message = "name을 입력하세요")
    @field:Schema(description = "회원 이름", example = "JangHyoSeok", required = true)
    val name: String,
) {
    fun toServiceDto() = MemberCreateDto(name = name)
}
