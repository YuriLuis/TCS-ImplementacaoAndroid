package com.yuri.luis.garcia.pereira.tcs_implementacao.model

import java.io.Serializable

data class Regra(
    var idRegra: Int?,
    var ordem: Int,
    var nome: String,
    val dataRegra: String?,
    val itens: MutableList<RegraItem>?,
    val resultados: MutableList<RegraItemResultado>?
): Serializable {

    constructor(): this(null, 0,"", null,  mutableListOf(),  mutableListOf())
}