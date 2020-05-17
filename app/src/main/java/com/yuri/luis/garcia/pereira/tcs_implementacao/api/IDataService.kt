package com.yuri.luis.garcia.pereira.tcs_implementacao.api

import com.yuri.luis.garcia.pereira.tcs_implementacao.model.Variavel
import com.yuri.luis.garcia.pereira.tcs_implementacao.model.VariavelValor
import retrofit2.http.*


interface IDataService {

    @GET("variavel")
    fun getVariavel(): retrofit2.Call<List<Variavel>>

    @GET("variavel/{idVariavel}")
    fun getVariavel(@Path("idVariavel") idVariavel : Int): retrofit2.Call<Variavel>

    @POST("variavel/salvaVariavel")
    fun postVariavel(@Body variavel: Variavel): retrofit2.Call<Variavel>

    @POST("variavel/adicionaValor/{idVariavel}")
    fun postValorVariavel(@Path("idVariavel") idVariavel : Int ,
                          @Body variavelValor: VariavelValor
    ): retrofit2.Call<VariavelValor>

}