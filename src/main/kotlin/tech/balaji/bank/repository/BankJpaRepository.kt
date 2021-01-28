package tech.balaji.bank.repository

import org.springframework.data.jpa.repository.JpaRepository
import tech.balaji.bank.model.Bank

interface BankJpaRepository : JpaRepository<Bank, Long>, BankRepository