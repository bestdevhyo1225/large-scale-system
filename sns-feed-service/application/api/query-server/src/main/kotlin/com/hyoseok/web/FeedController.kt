package com.hyoseok.web

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/feeds")
class FeedController {

    @GetMapping("/{memberId}")
    fun get(
        @PathVariable memberId: Long,
        @RequestParam start: Long,
        @RequestParam count: Long,
    ) {

    }
}
