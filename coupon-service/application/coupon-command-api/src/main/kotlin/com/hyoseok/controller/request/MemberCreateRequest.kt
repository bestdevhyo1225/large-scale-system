package com.hyoseok.controller.request

import com.hyoseok.service.dto.MemberCreateDto
import javax.validation.constraints.NotBlank

data class MemberCreateRequest(
    @field:NotBlank(message = "name을 입력하세요")
    val name: String,
) {
    fun toServiceDto() = MemberCreateDto(name = name)
}
