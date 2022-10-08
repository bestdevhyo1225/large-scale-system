package com.hyoseok.member.controller

import com.hyoseok.member.controller.request.MemberCreateRequest
import com.hyoseok.member.service.MemberService
import com.hyoseok.member.service.dto.MemberCreateResultDto
import com.hyoseok.response.SuccessResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import javax.validation.Valid

@RestController
@RequestMapping("/members")
class MemberController(
    private val memberService: MemberService,
) {

    @PostMapping
    fun create(
        @Valid @RequestBody
        request: MemberCreateRequest,
    ): ResponseEntity<SuccessResponse<MemberCreateResultDto>> {
        val memberCreateResultDto: MemberCreateResultDto = memberService.create(dto = request.toServiceDto())
        return ResponseEntity
            .created(URI.create("/members/${memberCreateResultDto.memberId}"))
            .body(SuccessResponse(data = memberCreateResultDto))
    }
}
