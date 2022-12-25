package com.hyoseok.controller

import com.hyoseok.post.dto.PostCacheDto
import com.hyoseok.post.dto.PostDto
import com.hyoseok.post.service.PostRedisReadService
import com.hyoseok.response.SuccessResponse
import com.hyoseok.usecase.FindPostsTimelineUsecase
import com.hyoseok.util.PageByPosition
import com.hyoseok.util.PageRequestByPosition
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/posts")
class PostController(
    private val findPostsTimelineUsecase: FindPostsTimelineUsecase,
    private val postRedisReadService: PostRedisReadService,
) {

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): ResponseEntity<SuccessResponse<PostCacheDto>> =
        ResponseEntity.ok(SuccessResponse(data = postRedisReadService.findPostCacheThrow(id = id)))

    @GetMapping("/members/{memberId}")
    fun getPosts(
        @PathVariable
        memberId: Long,
        pageRequestByPosition: PageRequestByPosition,
    ): ResponseEntity<SuccessResponse<PageByPosition<PostCacheDto>>> =
        ResponseEntity.ok(
            SuccessResponse(
                data = postRedisReadService.findPageOfPostCaches(
                    memberId = memberId,
                    pageRequestByPosition = pageRequestByPosition,
                ),
            ),
        )

    @GetMapping("/members/{memberId}/timeline")
    fun getTimeline(
        @PathVariable
        memberId: Long,
        pageRequestByPosition: PageRequestByPosition,
    ): ResponseEntity<SuccessResponse<PageByPosition<PostDto>>> =
        ResponseEntity.ok(
            SuccessResponse(
                data = findPostsTimelineUsecase.execute(
                    memberId,
                    pageRequestByPosition = pageRequestByPosition,
                ),
            ),
        )
}
