package com.yuri.luis.garcia.pereira.tcs_implementacao.config

import com.yuri.luis.garcia.pereira.tcs_implementacao.api.IDataService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInitializer {

    private val retrofit: Retrofit =  Retrofit.Builder()
            //.baseUrl("https://api-tcs.herokuapp.com/")
        .baseUrl("http://52.204.71.114/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    private val retrofitPython: Retrofit =  Retrofit.Builder()
        .baseUrl("http://52.204.71.114/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun variavelService() : IDataService {
        return retrofit.create(IDataService::class.java)
    }

    fun Service() : IDataService {
        return retrofit.create(IDataService::class.java)
    }

    fun ServicePython() : IDataService {
        return retrofitPython.create(IDataService::class.java)
    }
}