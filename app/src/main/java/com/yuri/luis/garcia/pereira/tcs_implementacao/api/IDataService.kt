package com.yuri.luis.garcia.pereira.tcs_implementacao.api

import com.yuri.luis.garcia.pereira.tcs_implementacao.model.Execucao
import com.yuri.luis.garcia.pereira.tcs_implementacao.model.ImageRetorno
import com.yuri.luis.garcia.pereira.tcs_implementacao.model.Variavel
import com.yuri.luis.garcia.pereira.tcs_implementacao.model.VariavelValor
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface IDataService {

    @GET("variavel")
    fun findAllVariaveis(): Call<List<Variavel>>

    @GET("variavel/{idVariavel}")
    fun findById(@Path("idVariavel") idVariavel: Int?): Call<Variavel>

    @POST("variavel/salvaVariavel")
    fun postVariavel(@Body variavel: Variavel): Call<Variavel>

    @POST("variavel/adicionaValor/{idVariavel}")
    fun postValorVariavel(
        @Path("idVariavel") idVariavel: Int,
        @Body variavelValor: VariavelValor
    ): Call<VariavelValor>

    @DELETE("variavel/{idVariavel}")
    fun deleteVariavel(@Path("idVariavel") idVariavel: Int): Call<Void>

    @DELETE("variavel/{idVariavel}")
    fun deleteValorVariavel(
        @Path("idVariavel") idVariavel: Int,
        @Body variavelValor: VariavelValor
    ): Call<Void>

    @POST("execucao/iniciaExecucao/{idimage}")
    fun iniciaExecucao(
        @Path("idimage") idimage: Int
    ): retrofit2.Call<Execucao>

    @POST("execucao/adicionaRespostas/{idexecucao}")
    fun adicionaRespostas(
        @Path("idexecucao") idexecucao: Int,
        @Body arrayRespostas: List<String>
    ): retrofit2.Call<Execucao>

    @POST("salvaExecucao")
    fun salvaExecucao(@Body v: Execucao): retrofit2.Call<Execucao>

    @GET("execucao/{id}")
    fun getExecucao(
        @Path("id") id: Int?
    ): retrofit2.Call<Execucao>

    @GET("variavel/valor/{id}")
    fun getVariavelValor(
        @Path("id") id: Int?
    ): retrofit2.Call<VariavelValor>

    @Multipart
    @POST("imagens")
    fun enviaImagem(
        @Part file: MultipartBody.Part,
        @Part("file") name: RequestBody
    ): retrofit2.Call<ImageRetorno>

    @GET("imagens")
    fun getImagem(): retrofit2.Call<ImageRetorno>

}