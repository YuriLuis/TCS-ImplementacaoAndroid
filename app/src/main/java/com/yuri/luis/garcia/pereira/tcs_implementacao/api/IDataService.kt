package com.yuri.luis.garcia.pereira.tcs_implementacao.api

import com.yuri.luis.garcia.pereira.tcs_implementacao.model.Variavel
import com.yuri.luis.garcia.pereira.tcs_implementacao.model.VariavelValor
import retrofit2.Call
import retrofit2.http.*


interface IDataService {

    @GET("variavel")
    fun findAllVariaveis(): Call<List<Variavel>>

    @GET("variavel/{idVariavel}")
    fun findAllVariaveis(@Path("idVariavel") idVariavel: Int): Call<Variavel>

    @POST("variavel/salvaVariavel")
    fun postVariavel(@Body variavel: Variavel): Call<Variavel>

    @POST("variavel/adicionaValor/{idVariavel}")
    fun postValorVariavel(
        @Path("idVariavel") idVariavel: Int,
        @Body variavelValor: VariavelValor
    ): Call<VariavelValor>

    @DELETE("variavel/{idVariavel}")
    fun deleteVariavel(@Path("idVariavel") idVariavel: Int) : Call<Void>


}