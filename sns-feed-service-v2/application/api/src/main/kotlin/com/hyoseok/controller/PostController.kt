package com.hyoseok.controller

import com.hyoseok.controller.dto.PostCreateRequestDto
import com.hyoseok.post.dto.PostDto
import com.hyoseok.response.SuccessResponse
import com.hyoseok.usecase.CreatePostUsecase
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import javax.validation.Valid

@RestController
@RequestMapping("/posts")
class PostController(
    private val createPostUsecase: CreatePostUsecase,
) {

    @PostMapping("/{memberId}")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @PathVariable
        memberId: Long,
        @Valid @RequestBody
        request: PostCreateRequestDto,
    ): ResponseEntity<SuccessResponse<PostDto>> {
        val postDto: PostDto =
            createPostUsecase.execute(createPostUsecaseDto = request.toUsecaseDto(memberId = memberId))
        return ResponseEntity.created(URI.create("/posts/${postDto.id}")).body(SuccessResponse(data = postDto))
    }
}
