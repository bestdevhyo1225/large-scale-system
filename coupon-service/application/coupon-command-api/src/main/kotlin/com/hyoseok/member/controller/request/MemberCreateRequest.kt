package com.hyoseok.member.controller.request

import com.hyoseok.member.service.dto.MemberCreateDto
import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.constraints.NotBlank

data class MemberCreateRequest(
    @field:NotBlank(message = "name을 입력하세요")
    @field:Schema(description = "회원 이름", example = "JangHyoSeok", required = true)
    val name: String,
) {
    fun toServiceDto() = MemberCreateDto(name = name)
}
