package com.hyoseok.web

import com.hyoseok.service.SnsFacadeService
import com.hyoseok.service.dto.SnsCreateResultDto
import com.hyoseok.web.request.SnsCreateRequest
import com.hyoseok.web.request.SnsEditRequest
import com.hyoseok.web.response.SuccessResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.created
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PatchMapping
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
    private val snsFacadeService: SnsFacadeService,
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @Valid @RequestBody
        request: SnsCreateRequest,
    ): ResponseEntity<SuccessResponse<SnsCreateResultDto>> {
        val snsCreateResultDto: SnsCreateResultDto = snsFacadeService.create(dto = request.toServiceDto())
        return created(URI.create("/api/v1/sns/${snsCreateResultDto.snsId}"))
            .body(SuccessResponse(data = snsCreateResultDto))
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun edit(
        @PathVariable
        id: Long,
        @Valid @RequestBody
        request: SnsEditRequest,
    ) {
        snsFacadeService.edit(dto = request.toServiceDto(id = id))
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) {
        snsFacadeService.delete(id = id)
    }
}
