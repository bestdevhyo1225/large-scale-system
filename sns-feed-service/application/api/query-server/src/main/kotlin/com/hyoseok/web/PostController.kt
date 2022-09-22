package com.hyoseok.web

import com.hyoseok.service.dto.PostFindResultDto
import com.hyoseok.service.post.PostService
import com.hyoseok.web.response.SuccessResponse
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/posts")
class PostController(
    private val postService: PostService,
) {

    @GetMapping("/{memberId}")
    fun get(
        @PathVariable memberId: Long,
        @RequestParam start: Long,
        @RequestParam count: Long,
    ): ResponseEntity<SuccessResponse<Map<String, Any>>> {
        val (total: Long, posts: List<PostFindResultDto>) = postService.find(
            memberId = memberId,
            start = start,
            count = count,
        )
        return ok(
            SuccessResponse(
                data = mapOf(
                    "items" to posts,
                    "start" to start,
                    "count" to count,
                    "total" to total,
                ),
            ),
        )
    }
}
