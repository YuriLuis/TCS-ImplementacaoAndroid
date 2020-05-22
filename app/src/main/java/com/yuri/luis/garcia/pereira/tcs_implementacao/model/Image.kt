package com.yuri.luis.garcia.pereira.tcs_implementacao.model

data class Image(
    val idImage: Int?,
    val nome: String?,
    val extensao: String?,
    val contornos: Int?,
    val manchas: Int?,
    val image: String?,
    val imageProc: String?
) {

    constructor():this( null, null, null, null, null, null, null)
}