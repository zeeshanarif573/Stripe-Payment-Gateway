package com.example.stripepayment.ui.model.payment_intent

data class Card(
    val installments: Any,
    val mandate_options: Any,
    val network: Any,
    val request_three_d_secure: String
)