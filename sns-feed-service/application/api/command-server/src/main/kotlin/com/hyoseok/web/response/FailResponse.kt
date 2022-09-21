package com.hyoseok.web.response

data class FailResponse(
    val status: String = "fail",
    val message: String,
)
