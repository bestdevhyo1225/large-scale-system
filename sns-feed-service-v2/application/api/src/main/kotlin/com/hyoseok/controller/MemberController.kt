package com.hyoseok.controller

import com.hyoseok.controller.dto.MemberCreateRequestDto
import com.hyoseok.member.dto.MemberDto
import com.hyoseok.member.service.MemberService
import com.hyoseok.response.SuccessResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import javax.validation.Valid

@RestController
@RequestMapping("/members")
class MemberController(
    private val memberService: MemberService,
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @Valid @RequestBody
        request: MemberCreateRequestDto,
    ): ResponseEntity<SuccessResponse<MemberDto>> {
        val memberDto: MemberDto = memberService.create(dto = request.toServiceDto())
        return ResponseEntity.created(URI.create("/members/${memberDto.id}")).body(SuccessResponse(data = memberDto))
    }
}
