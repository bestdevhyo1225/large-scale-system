package com.hyoseok.web

import com.hyoseok.service.UrlService
import com.hyoseok.web.request.CreateUrlRequest
import com.hyoseok.web.response.SuccessResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1")
class UrlController(
    private val urlService: UrlService,
) {

    @PostMapping("/url/shorten")
    fun create(
        @Valid @RequestBody
        request: CreateUrlRequest,
    ): ResponseEntity<SuccessResponse<Map<String, String>>> =
        ok(SuccessResponse(data = mapOf("shortUrl" to urlService.create(longUrl = request.longUrl))))

    @GetMapping("/{shortUrl}")
    fun get(@PathVariable shortUrl: String): ResponseEntity<SuccessResponse<Any>> {
        val httpHeader = HttpHeaders()
        httpHeader.location = URI.create(urlService.find(shortUrl = shortUrl))
        return ResponseEntity(httpHeader, HttpStatus.MOVED_PERMANENTLY)
    }
}
