package com.example.stripepayment.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stripepayment.ui.model.customer.CustomerResponse
import com.example.stripepayment.ui.model.empheral_keys.EphemeralKeysResponse
import com.example.stripepayment.ui.model.payment_intent.PaymentIntentResponse
import com.example.stripepayment.ui.repository.MainRepository
import com.example.stripepayment.utils.ResponseHandling
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val repository: MainRepository) : ViewModel() {

    fun getCustomers() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getStripeCustomer()
        }
    }

    val customerResponse: LiveData<ResponseHandling<CustomerResponse>>
        get() = repository.customerResponse


    fun getEphemeralKeys(customer_id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getEphemeralKeys(customer_id)
        }
    }

    val ephemeralResponse: LiveData<ResponseHandling<EphemeralKeysResponse>>
        get() = repository.ephemeralResponse


    fun getPaymentIntent(customer_id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getPaymentIntent(customer_id)
        }
    }

    val paymentIntentResponse: LiveData<ResponseHandling<PaymentIntentResponse>>
        get() = repository.paymentIntentResponse
}