package com.example.stripepayment.api_config

import com.example.stripepayment.ui.model.customer.CustomerResponse
import com.example.stripepayment.ui.model.empheral_keys.EphemeralKeysResponse
import com.example.stripepayment.ui.model.payment_intent.PaymentIntentResponse
import retrofit2.Response
import retrofit2.http.*

interface API {

    @POST(GET_CUSTOMERS)
    suspend fun getCustomers(
        @Header("authorization") authorization: String
    ): Response<CustomerResponse>

    @FormUrlEncoded
    @POST(EPHEMERAL_KEYS)
    suspend fun getEphemeralKeys(
        @Header("authorization") authorization: String,
        @Header("Stripe-Version") version: String,
        @Field("customer") customer: String
    ): Response<EphemeralKeysResponse>

    @FormUrlEncoded
    @POST(PAYMENT_INTENT)
    suspend fun getPaymentIntent(
        @Header("authorization") authorization: String,
        @Field("customer") customer: String,
        @Field("amount") amount: String,
        @Field("currency") currency: String,
        @Field("automatic_payment_methods[enabled]") automatic_payment_method: String
    ): Response<PaymentIntentResponse>

}