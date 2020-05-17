package com.yuri.luis.garcia.pereira.tcs_implementacao.model

data class Variavel(
    val idVariavel: Int?,
    val nome: String,
    val tipoVariavel: Int?,
    val valores: List<VariavelValor>
) {

        constructor(): this(null,
        "", null, emptyList())
}