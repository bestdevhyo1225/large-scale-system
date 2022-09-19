package com.hyoseok.web

import com.hyoseok.service.dto.PostCreateResultDto
import com.hyoseok.service.post.PostService
import com.hyoseok.web.request.PostCreateRequest
import com.hyoseok.web.response.SuccessResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.created
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.net.URI.create
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/posts")
class PostController(
    private val postService: PostService,
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody request: PostCreateRequest): ResponseEntity<SuccessResponse<PostCreateResultDto>> {
        val postCreateResultDto: PostCreateResultDto = postService.create(dto = request.toServiceDto())
        return created(create("/api/v1/posts/${postCreateResultDto.postId}"))
            .body(SuccessResponse(data = postCreateResultDto))
    }
}
