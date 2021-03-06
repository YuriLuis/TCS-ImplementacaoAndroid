package com.yuri.luis.garcia.pereira.tcs_implementacao.model

import java.time.LocalDateTime

data class Execucao(
    val idExecucao: Int?,
    val datahora: String?,
    val tipoVariavel: Int?,
    val regras: List<ExecucaoRegra>,
    val image: Image?
) {

    constructor(): this(null,
        null, null, emptyList(), null)
}