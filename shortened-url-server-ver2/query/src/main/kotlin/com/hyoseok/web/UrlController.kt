package com.hyoseok.web

import com.hyoseok.service.UrlFacadeService
import com.hyoseok.web.response.SuccessResponse
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class UrlController(
    private val urlFacadeService: UrlFacadeService,
) {

    @GetMapping("/{shortUrl}")
    fun get(@PathVariable shortUrl: String): ResponseEntity<SuccessResponse<Map<String, String>>> =
        ok(SuccessResponse(data = mapOf("longUrl" to urlFacadeService.find(shortUrl = shortUrl))))
}
