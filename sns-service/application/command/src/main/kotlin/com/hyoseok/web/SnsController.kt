package com.hyoseok.web

import com.hyoseok.service.SnsService
import com.hyoseok.service.dto.SnsCreateResultDto
import com.hyoseok.service.dto.SnsFindResultDto
import com.hyoseok.web.request.SnsCreateRequest
import com.hyoseok.web.response.SuccessResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.created
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/sns")
class SnsController(
    private val snsService: SnsService,
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @Valid @RequestBody
        request: SnsCreateRequest,
    ): ResponseEntity<SuccessResponse<SnsCreateResultDto>> {
        val snsCreateResultDto = snsService.create(dto = request.toServiceDto())
        return created(URI.create("/api/v1/sns/${snsCreateResultDto.snsId}"))
            .body(SuccessResponse(data = snsCreateResultDto))
    }

    @GetMapping("/{id}")
    fun getDetail(@PathVariable id: Long): ResponseEntity<SuccessResponse<SnsFindResultDto>> =
        ok(SuccessResponse(data = snsService.findWithAssociatedEntitiesById(snsId = id)))
}
