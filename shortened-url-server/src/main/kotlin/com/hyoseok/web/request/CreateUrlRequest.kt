package com.hyoseok.web.request

import javax.validation.constraints.NotBlank

data class CreateUrlRequest(
    @field:NotBlank(message = "longUrl을 입력하세요")
    val longUrl: String
)
