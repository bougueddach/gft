package com.gft.gft.infrastructure.api

import com.gft.generated.model.ErrorResponse
import com.gft.gft.domain.exceptions.PriceNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(PriceNotFoundException::class)
    fun handlePriceNotFoundException(ex: PriceNotFoundException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            status = HttpStatus.NOT_FOUND.value(),
            error = "Price Not Found",
            message = ex.message ?: "Requested price not available."
        )
        return ResponseEntity(errorResponse, HttpStatus.NOT_FOUND)
    }
}

