package com.example.stripepayment.ui.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.stripepayment.R
import com.example.stripepayment.api_config.API
import com.example.stripepayment.api_config.SECRET_KEY
import com.example.stripepayment.ui.model.customer.CustomerResponse
import com.example.stripepayment.ui.model.empheral_keys.EphemeralKeysResponse
import com.example.stripepayment.ui.model.payment_intent.PaymentIntentResponse
import com.example.stripepayment.utils.NetworkUtils
import com.example.stripepayment.utils.ResponseHandling
import com.google.gson.JsonParser
import java.lang.Exception

class MainRepository(
    private val api: API,
    private val applicationContext: Context
) {

    //customer
    private val customerLiveData = MutableLiveData<ResponseHandling<CustomerResponse>>()
    val customerResponse: LiveData<ResponseHandling<CustomerResponse>>
        get() = customerLiveData

    //ephemeral
    private val ephemeralLiveData = MutableLiveData<ResponseHandling<EphemeralKeysResponse>>()
    val ephemeralResponse: LiveData<ResponseHandling<EphemeralKeysResponse>>
        get() = ephemeralLiveData

    //payment_intent
    private val paymentIntentLiveData = MutableLiveData<ResponseHandling<PaymentIntentResponse>>()
    val paymentIntentResponse: LiveData<ResponseHandling<PaymentIntentResponse>>
        get() = paymentIntentLiveData


    suspend fun getStripeCustomer() {
        if (NetworkUtils.isInternetAvailable()) {
            try {
                val result = api.getCustomers(SECRET_KEY)
                if (result.isSuccessful) {
                    if (result.body() != null) {
                        customerLiveData.postValue(ResponseHandling.Success(result.body()))

                    } else
                        customerLiveData.postValue(
                            ResponseHandling.Error(
                                applicationContext.getString(
                                    R.string.wentWrong
                                )
                            )
                        )

                } else
                    customerLiveData.postValue(
                        ResponseHandling.Error(
                            applicationContext.getString(
                                R.string.wentWrong
                            )
                        )
                    )

            } catch (e: Exception) {
                e.printStackTrace()
                customerLiveData.postValue(ResponseHandling.Error(e.message))
            }

        } else
            customerLiveData.postValue(ResponseHandling.Error(applicationContext.getString(R.string.no_internet)))
    }

    suspend fun getEphemeralKeys(customer_id: String) {
        if (NetworkUtils.isInternetAvailable()) {
            try {
                val result = api.getEphemeralKeys(SECRET_KEY, "2020-08-27", customer_id)
                if (result.isSuccessful) {
                    if (result.body() != null) {
                        ephemeralLiveData.postValue(ResponseHandling.Success(result.body()))

                    } else
                        ephemeralLiveData.postValue(
                            ResponseHandling.Error(
                                applicationContext.getString(
                                    R.string.wentWrong
                                )
                            )
                        )

                } else
                    ephemeralLiveData.postValue(
                        ResponseHandling.Error(
                            applicationContext.getString(
                                R.string.wentWrong
                            )
                        )
                    )

            } catch (e: Exception) {
                e.printStackTrace()
                ephemeralLiveData.postValue(ResponseHandling.Error(e.message))
            }

        } else
            ephemeralLiveData.postValue(ResponseHandling.Error(applicationContext.getString(R.string.no_internet)))
    }

    suspend fun getPaymentIntent(customer_id: String) {
        if (NetworkUtils.isInternetAvailable()) {
            try {
                val result = api.getPaymentIntent(SECRET_KEY, customer_id, "1000", "usd", "true")
                if (result.isSuccessful) {
                    if (result.body() != null) {
                        paymentIntentLiveData.postValue(ResponseHandling.Success(result.body()))

                    } else
                        paymentIntentLiveData.postValue(
                            ResponseHandling.Error(
                                applicationContext.getString(
                                    R.string.wentWrong
                                )
                            )
                        )

                } else
                    paymentIntentLiveData.postValue(
                        ResponseHandling.Error(
                            applicationContext.getString(
                                R.string.wentWrong
                            )
                        )
                    )

            } catch (e: Exception) {
                e.printStackTrace()
                paymentIntentLiveData.postValue(ResponseHandling.Error(e.message))
            }

        } else
            paymentIntentLiveData.postValue(ResponseHandling.Error(applicationContext.getString(R.string.no_internet)))
    }

}
