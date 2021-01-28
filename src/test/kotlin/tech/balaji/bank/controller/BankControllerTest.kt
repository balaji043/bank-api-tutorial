package tech.balaji.bank.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.*
import tech.balaji.bank.model.Bank
import tech.balaji.bank.util.ID_MANDATORY
import tech.balaji.bank.util.accountDetailsNotAvailable
import tech.balaji.bank.util.accountNumAlreadyExists

@SpringBootTest
@AutoConfigureMockMvc
internal class BankControllerTest @Autowired constructor(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper
) {

    val baseUri = "/api/bank"

    @Nested
    @DisplayName("POST /api/bank")
    @TestInstance(PER_CLASS)
    @DirtiesContext
    inner class CreateNewBank {

        @Test
        fun `should add a new bank`() {
            /* given */
            val bank = Bank(
                accountNumber = "uniqueAccountNumber",
                transactionFee = 12.0,
                trustFee = 1.0,
            )

            /* when */
            val postPerformRequest = mockMvc.post(baseUri) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(bank)
            }

            bank.id = getBankId(postPerformRequest)

            /* Then */
            postPerformRequest
                .andDo { print() }
                .andExpect {
                    status { isCreated() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.id", equalTo(bank.id))
                    jsonPath("$.accountNumber", equalTo(bank.accountNumber))
                    jsonPath("$.transactionFee", equalTo(bank.transactionFee))
                    jsonPath("$.trustFee", equalTo(bank.trustFee))
                }
        }

        @Test
        fun `should give bad request as account number already exists`() {
            /* given */
            val bank = createBankToTest()

            /* When */
            val postPerformRequestWithExistingAccountNumber = mockMvc.post(baseUri) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(bank)
            }

            /* Then */
            postPerformRequestWithExistingAccountNumber
                .andDo { print() }
                .andExpect {
                    status {
                        status { isBadRequest() }
                        content {
                            string(accountNumAlreadyExists(bank.accountNumber))
                        }
                    }
                }
        }
    }

    @Nested
    @DisplayName("PUT /api/bank")
    @TestInstance(PER_CLASS)
    @DirtiesContext
    inner class UpdateAnExistingBank {

        @Test
        fun `should update the existing bank`() {
            /* given */
            val bank = createBankToTest()

            val updateBankValue = Bank(
                bank.id,
                bank.accountNumber,
                32123123.0,
                123.0,
            )

            /* when */
            val putPerformRequest = mockMvc.put(baseUri) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(updateBankValue)
            }

            /* Then */
            putPerformRequest
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.id", equalTo(updateBankValue.id))
                    jsonPath("$.accountNumber", equalTo(updateBankValue.accountNumber))
                    jsonPath("$.transactionFee", equalTo(updateBankValue.transactionFee))
                    jsonPath("$.trustFee", equalTo(updateBankValue.trustFee))
                }
        }

        @Test
        fun `should give bad request as id is null`() {
            /* given */
            val newBankWithExistingAccountNumber = Bank(
                accountNumber = "213",
                transactionFee = 12.0,
                trustFee = 1.0,
            )

            /* when */
            val postPerformRequest = mockMvc.put(baseUri) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(newBankWithExistingAccountNumber)
            }

            /* Then */
            postPerformRequest
                .andDo { print() }
                .andExpect {
                    status { isBadRequest() }
                    content { string(ID_MANDATORY) }
                }
        }

        @Test
        fun `should give bad request as account number does not exists`() {
            /* given */
            val bankWithAccountNumberNotExists = Bank(
                id = 1,
                accountNumber = "213",
                transactionFee = 12.0,
                trustFee = 1.0,
            )

            /* when */
            val postPerformRequest = mockMvc.put(baseUri) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(bankWithAccountNumberNotExists)
            }

            /* Then */
            postPerformRequest
                .andDo { print() }
                .andExpect {
                    status {
                        status { isNotFound() }
                        content {
                            accountDetailsNotAvailable(
                                bankWithAccountNumberNotExists.accountNumber,
                                bankWithAccountNumberNotExists.id,
                            )
                        }
                    }
                }
        }
    }


    @Nested
    @DisplayName("GET /api/bank/all")
    @TestInstance(PER_CLASS)
    @DirtiesContext
    inner class GetAllBanks {

        @Test
        fun `should get all the banks as array`() {
            /* given */
            val bank = createBankToTest()
            /* when */
            val getPerformRequest = mockMvc.get("${baseUri}/all")
            /* Then */
            getPerformRequest
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        jsonPath("$[0].id", equalTo(bank.id))
                        jsonPath("$[0].accountNumber", equalTo(bank.accountNumber))
                        jsonPath("$[0].transactionFee", equalTo(bank.transactionFee))
                        jsonPath("$[0].trustFee", equalTo(bank.trustFee))
                    }
                }
        }

    }

    @Nested
    @DisplayName("GET /api/bank/123")
    @TestInstance(PER_CLASS)
    @DirtiesContext
    inner class GetBankByAccountNumber {

        @Test
        fun `should Return a Bank`() {
            /* given */
            val bank = createBankToTest()
            /* when */
            val getPerformRequest = mockMvc.get("$baseUri/${bank.accountNumber}")
            /* Then */
            getPerformRequest
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)

                    }
                    jsonPath("$.id", equalTo(bank.id))
                    jsonPath("$.accountNumber", equalTo(bank.accountNumber))
                    jsonPath("$.transactionFee", equalTo(bank.transactionFee))
                    jsonPath("$.trustFee", equalTo(bank.trustFee))
                }
        }

        @Test
        fun `should throw bank details unavailable exception`() {
            /* given */
            val invalidAccountNumber = "DOES_NOT_EXISTS"
            /* when */
            val getPerformRequest = mockMvc.get("$baseUri/$invalidAccountNumber")
            /* Then */
            getPerformRequest
                .andDo { print() }
                .andExpect {
                    status { isNotFound() }
                    content {
                        accountDetailsNotAvailable(invalidAccountNumber)
                    }
                }
        }
    }

    @Nested
    @DisplayName("DELETE /api/bank/123")
    @TestInstance(PER_CLASS)
    @DirtiesContext
    inner class DeleteBankByAccountNumber {

        @Test
        @DirtiesContext
        fun `should delete the Bank`() {
            /* given */
            val bank = createBankToTest()
            /* when */
            val deletePerformRequest = mockMvc.delete("$baseUri/${bank.accountNumber}")
            /* Then */
            deletePerformRequest
                .andDo { print() }
                .andExpect {
                    status { isNoContent() }
                }
        }

        @Test
        fun `should throw bank details unavailable exception`() {
            /* given */
            val invalidAccountNumber = "DOES_NOT_EXISTS"
            /* when */
            val deletePerformRequest = mockMvc.delete("$baseUri/$invalidAccountNumber")
            /* Then */
            deletePerformRequest
                .andDo { print() }
                .andExpect {
                    status { isNotFound() }
                    content {
                        accountDetailsNotAvailable(invalidAccountNumber)
                    }
                }
        }
    }

    private fun createBankToTest(): Bank {
        val bank = Bank(
            accountNumber = "123",
            transactionFee = 14.0,
            trustFee = 1.0,
        )

        val postPerformRequest = mockMvc.post(baseUri) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(bank)
        }

        bank.id = getBankId(postPerformRequest)
        postPerformRequest.andDo { print() }
            .andExpect {
                status { isCreated() }
            }
        return bank
    }

    private fun getBankId(postPerformRequest: ResultActionsDsl): Int? {
        return objectMapper.readValue<Bank>(
            postPerformRequest
                .andReturn()
                .response
                .contentAsString
        ).id
    }

}