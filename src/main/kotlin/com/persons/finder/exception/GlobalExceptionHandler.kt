package com.persons.finder.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleJsonErrors(ex: HttpMessageNotReadableException): ResponseEntity<Map<String, String>> {
        return ResponseEntity(
            mapOf("error" to "Malformed JSON request", "details" to (ex.message ?: "Invalid format")),
            HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(value =[MethodArgumentNotValidException::class])
    fun handleValidationErrors(ex: MethodArgumentNotValidException): ResponseEntity<Map<String, String>> {
        println("Handler reached! Errors: ${ex.bindingResult.fieldErrorCount}")
        val errors = ex.bindingResult.fieldErrors.associate {
            it.field to (it.defaultMessage ?: "Invalid value")
        }
        return ResponseEntity(errors, HttpStatus.BAD_REQUEST)
    }

    // 2. Handles generic "Not Found" logic
    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(ex: NoSuchElementException): ResponseEntity<Map<String, String>> {
        return ResponseEntity(mapOf("error" to "Resource not found --> ${ex.message}"), HttpStatus.NOT_FOUND)
    }
}