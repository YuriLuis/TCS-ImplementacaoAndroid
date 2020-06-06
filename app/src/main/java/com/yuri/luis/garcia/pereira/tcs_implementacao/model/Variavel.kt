package com.yuri.luis.garcia.pereira.tcs_implementacao.model

import java.io.Serializable

data class Variavel (
    val idVariavel: Int?,
    val nome: String,
    val tipoVariavel: Int?,
    val valores: List<VariavelValor>
) : Serializable {

        constructor(): this(null,
        "", null, emptyList())

    public override fun toString() : String {
        return this.nome
    }
}