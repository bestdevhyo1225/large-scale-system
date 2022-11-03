package com.hyoseok.controller

import com.hyoseok.controller.dto.FollowCreateRequestDto
import com.hyoseok.follow.dto.FollowDto
import com.hyoseok.response.SuccessResponse
import com.hyoseok.usecase.CreateFollowMemberUsecase
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
@RequestMapping("/follow")
class FollowController(
    private val createFollowMemberUsecase: CreateFollowMemberUsecase,
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @Valid @RequestBody
        request: FollowCreateRequestDto,
    ): ResponseEntity<SuccessResponse<FollowDto>> {
        val followDto: FollowDto = createFollowMemberUsecase.execute(
            followerId = request.followerId,
            followeeId = request.followeeId,
        )
        return ResponseEntity.created(URI.create("/follows/${followDto.id}")).body(SuccessResponse(data = followDto))
    }
}
