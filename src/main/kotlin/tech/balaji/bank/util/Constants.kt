package tech.balaji.bank.util

import org.springframework.http.HttpStatus
import tech.balaji.bank.exception.BankApiException

const val ID_MANDATORY = "ID is mandatory"

fun accountNumAlreadyExists(accountNumber: String): String = "Account Number $accountNumber already Exists"

fun accountDetailsNotAvailable(
    accountNumber: String,
    id: Int? = null,
): String =
    "Bank details are not available for the given account number $accountNumber ${if (id != null) "/ ID : $id" else ""}"

fun throwAccountDetailsNotAvailable(
    accountNumber: String,
    id: Int? = null,
): Nothing =
    throw BankApiException(
        message = accountDetailsNotAvailable(accountNumber, id),
        status = HttpStatus.NOT_FOUND,
    )


fun throwAccountNumAlreadyExists(accountNumber: String): Nothing =
    throw BankApiException(
        message = accountNumAlreadyExists(accountNumber),
        status = HttpStatus.BAD_REQUEST
    )

fun throwMandatoryValueMissingException(message: String): Nothing =
    throw BankApiException(
        message = message,
        status = HttpStatus.BAD_REQUEST,
    )