package tech.balaji.bank.repository

import org.springframework.stereotype.Repository
import tech.balaji.bank.model.Bank
import java.util.*

@Repository
interface BankRepository {

    fun findByAccountNumber(accountNumber: String): Optional<Bank>

    fun findAll(): Collection<Bank>

    fun save(bank: Bank): Bank

    fun findById(id: Long): Optional<Bank>

    fun delete(get: Bank)

}