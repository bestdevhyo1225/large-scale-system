package com.hyoseok.web

import com.hyoseok.service.dto.PostFindResultDto
import com.hyoseok.service.feed.FeedService
import com.hyoseok.web.response.SuccessResponse
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/feeds")
class FeedController(
    private val feedService: FeedService,
) {

    @GetMapping("/{memberId}/posts")
    fun get(
        @PathVariable memberId: Long,
        @RequestParam start: Long,
        @RequestParam count: Long,
    ): ResponseEntity<SuccessResponse<List<PostFindResultDto>>> {
        val posts: List<PostFindResultDto> = feedService.find(memberId = memberId, start = start, count = count)
        return ok(SuccessResponse(data = posts))
    }
}
