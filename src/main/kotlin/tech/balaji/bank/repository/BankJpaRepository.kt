package tech.balaji.bank.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import tech.balaji.bank.model.Bank

@Repository
interface BankJpaRepository : JpaRepository<Bank, Int>, BankRepository