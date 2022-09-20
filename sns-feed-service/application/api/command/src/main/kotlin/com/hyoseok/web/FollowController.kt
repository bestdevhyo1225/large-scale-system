package com.hyoseok.web

import com.hyoseok.service.dto.FollowCreateResultDto
import com.hyoseok.service.follow.FollowService
import com.hyoseok.web.request.FollowCreateRequest
import com.hyoseok.web.response.SuccessResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/follows")
class FollowController(
    private val followService: FollowService,
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @Valid @RequestBody
        request: FollowCreateRequest,
    ): ResponseEntity<SuccessResponse<FollowCreateResultDto>> {
        val followCreateResultDto: FollowCreateResultDto = followService.following(dto = request.toServiceDto())
        return ResponseEntity.created(URI.create("/api/v1/follows/${followCreateResultDto.followId}"))
            .body(SuccessResponse(data = followCreateResultDto))
    }
}
