package com.hyoseok.web

import com.hyoseok.service.SnsFacadeService
import com.hyoseok.service.dto.SnsFindResultDto
import com.hyoseok.web.response.SuccessResponse
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/sns")
class SnsController(
    private val snsFacadeService: SnsFacadeService,
) {

    @GetMapping("/{id}")
    fun getDetail(@PathVariable id: Long): ResponseEntity<SuccessResponse<SnsFindResultDto>> =
        ok(SuccessResponse(data = snsFacadeService.findWithAssociatedEntitiesById(snsId = id)))

    @GetMapping
    fun get(
        @RequestParam("start") start: Long,
        @RequestParam("count") count: Long,
    ): ResponseEntity<SuccessResponse<Map<String, Any>>> {
        val (snsFindResultDto: List<SnsFindResultDto>, totalCount: Long) = snsFacadeService.findAllByLimitAndOffset(
            start = start,
            count = count,
        )
        return ok(
            SuccessResponse(
                data = mapOf(
                    "items" to snsFindResultDto,
                    "start" to start,
                    "count" to count,
                    "total" to totalCount,
                ),
            ),
        )
    }
}
