package tech.balaji.bank.controller

import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import tech.balaji.bank.model.Bank
import tech.balaji.bank.service.BankService

@RestController
@RequestMapping("/api/bank")
class BankController(val bankService: BankService) {

    @GetMapping("/all")
    fun getBanks(): Collection<Bank> = bankService.getBanks()

    @GetMapping("{accountNumber}")
    fun getBank(@PathVariable accountNumber: String): Bank = bankService.getBank(accountNumber)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createBank(@Validated @RequestBody bank: Bank): Bank = bankService.createBank(bank)

    @PutMapping
    fun updateBank(@RequestBody bank: Bank): Bank = bankService.updateBank(bank)

    @DeleteMapping("{accountNumber}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteBank(@PathVariable accountNumber: String) = bankService.deleteBank(accountNumber)

}