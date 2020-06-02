package com.yuri.luis.garcia.pereira.tcs_implementacao.model

data class FotoRetorno(
    val id_image: Int?,
    val status: String?
) {

    constructor():this( null, null)
}