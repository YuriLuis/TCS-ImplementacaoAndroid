package com.yuri.luis.garcia.pereira.tcs_implementacao.model

import java.io.Serializable

data class VariavelValor(
    var idVariavelValor: Int?,
    var valor: String,
    var variavel: Variavel?
                    ): Serializable {

    constructor(): this(null,
        "", null)
}