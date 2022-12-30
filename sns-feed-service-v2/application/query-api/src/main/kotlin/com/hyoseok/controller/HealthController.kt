package com.hyoseok.controller

import com.hyoseok.response.SuccessResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/health")
class HealthController {

    @GetMapping
    fun health(): ResponseEntity<SuccessResponse<String>> = ResponseEntity.ok(SuccessResponse(data = "health"))
}
