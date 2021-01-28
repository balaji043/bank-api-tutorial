package tech.balaji.bank.service

import tech.balaji.bank.model.Bank

interface BankService {

    fun getBanks(): Collection<Bank>

    fun getBank(accountNumber: String): Bank

    fun createBank(bank: Bank): Bank

    fun updateBank(bank: Bank): Bank

    fun deleteBank(accountNumber: String)

}
