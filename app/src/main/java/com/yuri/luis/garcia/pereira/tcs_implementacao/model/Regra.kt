package com.yuri.luis.garcia.pereira.tcs_implementacao.model

import java.io.Serializable

data class Regra(
    val idRegra: Int?,
    val nome: String,
    val dataRegra: String?,
    val itens: List<RegraItem>?,
    val resultados: List<RegraItemResultado>?
): Serializable {

    constructor(): this(null, "", null,  emptyList(),  emptyList())
}