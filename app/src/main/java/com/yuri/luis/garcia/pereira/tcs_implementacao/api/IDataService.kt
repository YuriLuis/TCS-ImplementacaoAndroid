package com.yuri.luis.garcia.pereira.tcs_implementacao.api

import com.yuri.luis.garcia.pereira.tcs_implementacao.model.*
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

    @POST("variavel/deletaValor/{idVariavel}")
    fun deleteValorVariavel(
        @Path("idVariavel") idVariavel: Int,
        @Body variavelValor: VariavelValor
    ): Call<Void>

    @POST("execucao/iniciaExecucao/{idimage}")
    fun iniciaExecucao(@Path("idimage") idimage : Int
    ): retrofit2.Call<Execucao>

    @POST("execucao/adicionaRespostas/{idexecucao}")
    fun adicionaRespostas(@Path("idexecucao") idexecucao: Int, @Body arrayRespostas: List<String>): retrofit2.Call<Execucao>

    @POST("salvaExecucao")
    fun salvaExecucao(@Body v: Execucao): retrofit2.Call<Execucao>

    @GET("execucao/{id}")
    fun getExecucao(@Path("id") id : Int?
    ): retrofit2.Call<Execucao>

    @GET("variavel/valor/{id}")
    fun getVariavelValor(@Path("id") id : Int?
    ): retrofit2.Call<VariavelValor>

    @GET("foto/{id}")
    fun getFoto(@Path("id") id : Int?
    ): retrofit2.Call<Foto>

    @Multipart
    @POST("imagens")
    fun enviaImagem(@Part file: MultipartBody.Part, @Part("file") name: RequestBody): retrofit2.Call<FotoRetorno>

    @GET("imagens")
    fun getImagem(): retrofit2.Call<FotoRetorno>

    @GET("/execucao/tomadaDecisao/{id}")
    fun getTomadaDecisao(@Path("id") id : Int?
    ): retrofit2.Call<Execucao>

    @GET("regra/{idRegra}")
    fun RegrafindById(@Path("idRegra") idRegra: Int?): Call<Regra>

    @GET("regra")
    fun findAllRegra(): Call<List<Regra>>

    @DELETE("regra/{idRegra}")
    fun deleteRegra(@Path("idRegra") idRegra: Int): Call<Void>

    @GET("regra/perguntas")
    fun findAllPerguntas(): Call<List<Regra>>

    @POST("regra/salvaRegra")
    fun postRegra(@Body regra: Regra) : Call<Regra>

    @POST("regra/adicionaItem/{idregra}")
    fun postRegraItem(@Path("idregra") idregra: Int, @Body item: RegraItem) : Call<RegraItem>

    @POST("regra/deletaItem/{idregra}")
    fun deleteRegraItem(
        @Path("idregra") idregra: Int,
        @Body item: RegraItem
    ): Call<Void>

    @POST("regra/adicionaItemResultado/{idregra}")
    fun postRegraItemResultado(@Path("idregra") idregra: Int, @Body item: RegraItemResultado) : Call<RegraItemResultado>

    @POST("regra/deletaItemResultado/{idregra}")
    fun deleteRegraItemResultado(
        @Path("idregra") idregra: Int,
        @Body item: RegraItemResultado
    ): Call<Void>

    @POST("interface/salvaInterface")
    fun postInterface(@Body objInterface: Interface) : Call<Interface>

    @GET("interface")
    fun findAllInterface(): Call<List<Interface>>

    @GET("interface/{id}")
    fun findByIdInterface(@Path("id") id: Int?): Call<Interface>

    @GET("interface/variavel/{idVariavel}")
    fun findInterfaceVariavel(@Path("idVariavel") idVariavel: Int?): Call<List<Variavel>>

    @DELETE("interface/{id}")
    fun deleteInterface(@Path("id") id: Int): Call<Void>
}