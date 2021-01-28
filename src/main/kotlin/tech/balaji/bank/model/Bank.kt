package tech.balaji.bank.model

import com.sun.istack.NotNull
import javax.persistence.*

@Entity
data class Bank(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int? = null,

    @Column(nullable = false, unique = true)
    @field:NotNull
    val accountNumber: String,

    @Column(nullable = false)
    @field:NotNull
    val transactionFee: Double,

    @Column(nullable = false)
    @field:NotNull
    val trustFee: Double,
) {

}