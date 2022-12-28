package com.hyoseok.controller

import com.hyoseok.post.dto.PostDto
import com.hyoseok.response.SuccessResponse
import com.hyoseok.usecase.FindPostUsecase
import com.hyoseok.usecase.FindPostsRefreshTimelineUsecase
import com.hyoseok.usecase.FindPostsTimelineUsecase
import com.hyoseok.usecase.FindPostsUsecase
import com.hyoseok.util.PageByPosition
import com.hyoseok.util.PageRequestByPosition
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/posts")
class PostController(
    private val findPostsRefreshTimelineUsecase: FindPostsRefreshTimelineUsecase,
    private val findPostsTimelineUsecase: FindPostsTimelineUsecase,
    private val findPostsUsecase: FindPostsUsecase,
    private val findPostUsecase: FindPostUsecase,
) {

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): ResponseEntity<SuccessResponse<PostDto>> =
        ResponseEntity.ok(SuccessResponse(data = findPostUsecase.execute(postId = id)))

    @GetMapping("/members/{memberId}")
    fun getPosts(
        @PathVariable
        memberId: Long,
        pageRequestByPosition: PageRequestByPosition,
    ): ResponseEntity<SuccessResponse<PageByPosition<PostDto>>> =
        ResponseEntity.ok(
            SuccessResponse(
                data = findPostsUsecase.execute(
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
                    memberId = memberId,
                    pageRequestByPosition = pageRequestByPosition,
                ),
            ),
        )

    @PostMapping("/members/{memberId}/refresh/timeline")
    fun refreshTimeline(@PathVariable memberId: Long): ResponseEntity<SuccessResponse<String>> {
        findPostsRefreshTimelineUsecase.execute(memberId = memberId)
        return ResponseEntity.ok(SuccessResponse(data = "refresh completed"))
    }
}
