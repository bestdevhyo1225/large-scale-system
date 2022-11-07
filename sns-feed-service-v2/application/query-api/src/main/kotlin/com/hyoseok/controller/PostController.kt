package com.hyoseok.controller

import com.hyoseok.post.dto.PostDto
import com.hyoseok.response.SuccessResponse
import com.hyoseok.usecase.FindPostUsecase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/posts")
class PostController(
    private val findPostUsecase: FindPostUsecase,
) {

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): ResponseEntity<SuccessResponse<PostDto>> =
        ResponseEntity.ok(SuccessResponse(data = findPostUsecase.execute(postId = id)))
}
