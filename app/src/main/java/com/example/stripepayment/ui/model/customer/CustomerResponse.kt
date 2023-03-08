package com.example.stripepayment.ui.model.customer

data class CustomerResponse(
    val address: String,
    val balance: Int,
    val created: Int,
    val currency: String,
    val default_currency: String,
    val default_source: String,
    val delinquent: Boolean,
    val description: String,
    val discount: String,
    val email: String,
    val id: String,
    val invoice_prefix: String,
    val invoice_settings: InvoiceSettings,
    val livemode: Boolean,
    val name: Any,
    val next_invoice_sequence: Int,
    val `object`: String,
    val phone: String,
    val preferred_locales: List<Any>,
    val shipping: String,
    val tax_exempt: String,
    val test_clock: String
)