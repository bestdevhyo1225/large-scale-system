package com.hyoseok

import com.hyoseok.controller.dto.ErrorResponseDto
import com.hyoseok.controller.dto.FailResponseDto
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import javax.validation.ConstraintViolationException

@RestControllerAdvice
class SnsFeedApiGlobalExceptionHandler {

    private val logger = KotlinLogging.logger {}

    @ExceptionHandler(IllegalArgumentException::class)
    fun handle(exception: IllegalArgumentException): ResponseEntity<FailResponseDto> {
        logger.error { exception }

        return ResponseEntity(FailResponseDto(message = exception.localizedMessage), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handle(exception: ConstraintViolationException): ResponseEntity<FailResponseDto> {
        logger.error { exception }

        return ResponseEntity(FailResponseDto(message = exception.localizedMessage), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handle(exception: MethodArgumentNotValidException): ResponseEntity<FailResponseDto> {
        logger.error { exception }

        return ResponseEntity(
            FailResponseDto(message = exception.bindingResult.allErrors.first().defaultMessage ?: ""),
            HttpStatus.BAD_REQUEST,
        )
    }

    @ExceptionHandler(NoSuchElementException::class)
    fun handle(exception: NoSuchElementException): ResponseEntity<FailResponseDto> {
        logger.error { exception }

        return ResponseEntity(FailResponseDto(message = exception.localizedMessage), HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(Exception::class)
    fun handle(exception: Exception): ResponseEntity<ErrorResponseDto> {
        logger.error { exception }

        return ResponseEntity(
            ErrorResponseDto(message = HttpStatus.INTERNAL_SERVER_ERROR.name),
            HttpStatus.INTERNAL_SERVER_ERROR,
        )
    }
}
