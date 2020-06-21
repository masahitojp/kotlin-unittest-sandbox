package com.github.masahito.kotestsandbox.businesslogics

import java.time.LocalDate

// inspired by https://gakuzzzz.github.io/slides/property_based_testing_for_domain
data class Item(
    val id: Long,
    val name: String
)

data class Order(val item: Item)

enum class ChargeResult {
    SUCCESS,
    Expired
}

data class CreditCard(
    val number: String,
    val limit: LocalDate,
    val holder: String
) {
    fun charge(order: Order, today: LocalDate? =LocalDate.now()): ChargeResult {
        // TODO: rewrite it, it's a restricted example.
        return if (this.limit < today){
            ChargeResult.Expired
        } else {
            ChargeResult.SUCCESS
        }
    }
}