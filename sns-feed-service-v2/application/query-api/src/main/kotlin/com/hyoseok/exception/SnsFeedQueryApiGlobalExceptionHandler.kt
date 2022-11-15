package com.hyoseok.exception

import com.hyoseok.response.ErrorResponse
import com.hyoseok.response.FailResponse
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import javax.validation.ConstraintViolationException

@RestControllerAdvice
class SnsFeedQueryApiGlobalExceptionHandler {

    private val logger = KotlinLogging.logger {}

    @ExceptionHandler(IllegalArgumentException::class)
    fun handle(exception: IllegalArgumentException): ResponseEntity<FailResponse> {
        logger.error { exception }

        return ResponseEntity(FailResponse(message = exception.localizedMessage), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handle(exception: ConstraintViolationException): ResponseEntity<FailResponse> {
        logger.error { exception }

        return ResponseEntity(FailResponse(message = exception.localizedMessage), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handle(exception: MethodArgumentNotValidException): ResponseEntity<FailResponse> {
        logger.error { exception }

        return ResponseEntity(
            FailResponse(message = exception.bindingResult.allErrors.first().defaultMessage ?: ""),
            HttpStatus.BAD_REQUEST,
        )
    }

    @ExceptionHandler(NoSuchElementException::class)
    fun handle(exception: NoSuchElementException): ResponseEntity<FailResponse> {
        logger.error { exception }

        return ResponseEntity(FailResponse(message = exception.localizedMessage), HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(QueryApiRateLimitException::class)
    fun handle(exception: QueryApiRateLimitException): ResponseEntity<FailResponse> {
        logger.error { exception }

        return ResponseEntity(FailResponse(message = exception.localizedMessage), HttpStatus.SERVICE_UNAVAILABLE)
    }

    @ExceptionHandler(Exception::class)
    fun handle(exception: Exception): ResponseEntity<ErrorResponse> {
        logger.error { exception }

        return ResponseEntity(
            ErrorResponse(message = HttpStatus.INTERNAL_SERVER_ERROR.name),
            HttpStatus.INTERNAL_SERVER_ERROR,
        )
    }
}
