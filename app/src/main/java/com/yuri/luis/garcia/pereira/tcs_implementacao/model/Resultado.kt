package com.yuri.luis.garcia.pereira.tcs_implementacao.model

data class Resultado(
    val idImage: Int?,
    val nome: String?,
    val extensao: String?,
    val contornos: Int?,
    val manchas: Int?,
    val image: String?,
    val image_proc: String?,
    val concluido: String?,
    val percentualAcerto: Double?
) {

    constructor():this( null, null, null, null, null, null, null,null, null)
}