package com.example.stripepayment.ui.view

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.stripepayment.R
import com.example.stripepayment.api_config.PUBLISH_KEY
import com.example.stripepayment.databinding.ActivityMainBinding
import com.example.stripepayment.ui.view_model.MainViewModel
import com.example.stripepayment.ui.view_model.MainViewModelFactory
import com.example.stripepayment.utils.ResponseHandling
import com.example.stripepayment.utils.StripeApplication
import com.google.android.material.snackbar.Snackbar
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var progressDialog: ProgressDialog
    private var customerID = ""
    private var ephemeralID = ""
    private var clientSecretID = ""
    lateinit var paymentSheet: PaymentSheet
    lateinit var customerConfig: PaymentSheet.CustomerConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val repository = (application as StripeApplication).mainRepository
        mainViewModel =
            ViewModelProvider(this, MainViewModelFactory(repository))[MainViewModel::class.java]
        initialize()

        binding.test.setOnClickListener {
            progressDialog.show()
            mainViewModel.getCustomers()
            getCustomerObserver()
        }
    }

    private fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
        when (paymentSheetResult) {
            is PaymentSheetResult.Canceled -> {
                Log.e("PaymentSheetResult", "Canceled")
            }
            is PaymentSheetResult.Failed -> {
                Log.e("PaymentSheetResult", "Error: ${paymentSheetResult.error}")
            }
            is PaymentSheetResult.Completed -> {
                Log.e("PaymentSheetResult", "Completed")
            }
        }
    }

    private fun initialize() {
        PaymentConfiguration.init(this, PUBLISH_KEY)
        paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage(getString(R.string.plz_wait))
        progressDialog.setCancelable(false)
    }

    private fun getCustomerObserver() {
        mainViewModel.customerResponse.observe(this) {
            when (it) {
                is ResponseHandling.Success -> {
                    customerID = it.data?.id!!
                    Log.e("customerID", customerID)
                    mainViewModel.getEphemeralKeys(customerID)
                    getEphemeralObserver()
                }
                is ResponseHandling.Error -> {
                    progressDialog.dismiss()
                    Snackbar.make(
                        binding.mainContainer,
                        it.errorMessage!!,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun getEphemeralObserver() {
        mainViewModel.ephemeralResponse.observe(this) {
            when (it) {
                is ResponseHandling.Success -> {
                    ephemeralID = it.data?.id!!
                    Log.e("EphemeralID", ephemeralID)
                    mainViewModel.getPaymentIntent(customerID)
                    getPaymentIntentObserver()
                }
                is ResponseHandling.Error -> {
                    progressDialog.dismiss()
                    Snackbar.make(
                        binding.mainContainer,
                        it.errorMessage!!,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun getPaymentIntentObserver() {
        mainViewModel.paymentIntentResponse.observe(this) {
            when (it) {
                is ResponseHandling.Success -> {
                    Log.e("object", it.data?.`object`!!)
                    clientSecretID = it.data.client_secret
                    progressDialog.dismiss()
                    paymentFlow()
                }
                is ResponseHandling.Error -> {
                    progressDialog.dismiss()
                    Snackbar.make(
                        binding.mainContainer,
                        it.errorMessage!!,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun paymentFlow() {
        customerConfig = PaymentSheet.CustomerConfiguration(
            customerID,
            ephemeralID
        )

        paymentSheet.presentWithPaymentIntent(
            clientSecretID,
            PaymentSheet.Configuration(
                merchantDisplayName = "Bidfeed",
                customer = customerConfig,
                // Set `allowsDelayedPaymentMethods` to true if your business
                // can handle payment methods that complete payment after a delay, like SEPA Debit and Sofort.
                allowsDelayedPaymentMethods = true
            )
        )
    }
}