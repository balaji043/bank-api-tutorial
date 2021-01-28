package tech.balaji.bank.exception

import org.springframework.http.HttpStatus

class BankApiException(
    val status: HttpStatus,
    override val message: String?
) : RuntimeException(message) {

}