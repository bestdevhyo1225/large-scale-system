package com.hyoseok.web

import com.hyoseok.service.UrlService
import com.hyoseok.web.request.CreateUrlRequest
import com.hyoseok.web.response.SuccessResponse
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.transaction.UnexpectedRollbackException
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
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
    ): ResponseEntity<SuccessResponse<Map<String, String>>> {
        return try {
            val encodedUrl = urlService.create(longUrl = request.longUrl)
            ok(SuccessResponse(data = mapOf("encodedUrl" to encodedUrl)))
        } catch (exception: UnexpectedRollbackException) {
            val encodedUrl = urlService.findByEncodedUrl(longUrl = request.longUrl)
            ok(SuccessResponse(data = mapOf("encodedUrl" to encodedUrl)))
        }
    }
}
