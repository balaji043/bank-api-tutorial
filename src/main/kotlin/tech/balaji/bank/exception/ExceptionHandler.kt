package tech.balaji.bank.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.lang.Exception


@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(BankApiException::class)
    fun handleNoSuchElementException(e: BankApiException): ResponseEntity<String> =
        ResponseEntity<String>(e.message, e.status)

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<String> =
        ResponseEntity<String>(e.message, HttpStatus.INTERNAL_SERVER_ERROR)

}