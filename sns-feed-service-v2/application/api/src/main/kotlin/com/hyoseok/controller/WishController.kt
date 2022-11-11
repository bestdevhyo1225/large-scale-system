package com.hyoseok.controller

import com.hyoseok.controller.dto.WishCreateRequestDto
import com.hyoseok.usecase.CreateWishUsecase
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/wish")
class WishController(
    private val createWishUsecase: CreateWishUsecase,
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @Valid @RequestBody
        request: WishCreateRequestDto,
    ) {
        createWishUsecase.execute(postId = request.postId, memberId = request.memberId)
    }
}
