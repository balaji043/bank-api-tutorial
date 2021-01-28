package tech.balaji.bank.service

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import tech.balaji.bank.exception.BankApiException
import tech.balaji.bank.model.Bank
import tech.balaji.bank.repository.BankRepository
import tech.balaji.bank.util.ID_MANDATORY
import tech.balaji.bank.util.throwAccountDetailsNotAvailable
import tech.balaji.bank.util.throwAccountNumAlreadyExists
import tech.balaji.bank.util.throwMandatoryValueMissingException
import java.util.*

@Service
class BankServiceImpl(val bankRepository: BankRepository) : BankService {

    override fun getBanks(): Collection<Bank> = bankRepository.findAll()

    override fun getBank(accountNumber: String): Bank =
        bankRepository.findByAccountNumber(accountNumber)
            .orElseThrow {
                throwAccountDetailsNotAvailable(accountNumber)
            }


    override fun createBank(bank: Bank): Bank {
        val optional = bankRepository.findByAccountNumber(accountNumber = bank.accountNumber)
        if (optional.isPresent)
            throwAccountNumAlreadyExists(bank.accountNumber)
        return bankRepository.save(bank)
    }

    override fun updateBank(bank: Bank): Bank {
        if (bank.id == null)
            throwMandatoryValueMissingException(ID_MANDATORY)

        val optionalAccountNumber: Optional<Bank> =
            bankRepository.findByAccountNumber(accountNumber = bank.accountNumber)

        val optionalId: Optional<Bank> = bankRepository.findById(bank.id!!)

        if (optionalAccountNumber.isEmpty || optionalId.isEmpty)
            throwAccountDetailsNotAvailable(bank.accountNumber)

        return bankRepository.save(bank)
    }

    override fun deleteBank(accountNumber: String) {
        val optionalAccountNumber = bankRepository.findByAccountNumber(accountNumber)
        if (optionalAccountNumber.isEmpty)
            throwAccountDetailsNotAvailable(accountNumber)
        bankRepository.delete(optionalAccountNumber.get())
    }

}