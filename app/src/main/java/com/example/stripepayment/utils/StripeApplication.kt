package com.example.stripepayment.utils

import android.app.Application
import android.content.Context
import com.example.stripepayment.api_config.API
import com.example.stripepayment.api_config.RetrofitHelper
import com.example.stripepayment.ui.repository.MainRepository

class StripeApplication : Application() {

    lateinit var mainRepository: MainRepository

    companion object {
        private lateinit var appContext: Context
        fun getCtx(): Context {
            return appContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext;
        init()
    }

    private fun init() {
        val apiService = RetrofitHelper.invoke().create(API::class.java)

        mainRepository = MainRepository(apiService, applicationContext)
    }
}