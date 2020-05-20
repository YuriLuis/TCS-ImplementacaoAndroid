package com.yuri.luis.garcia.pereira.tcs_implementacao.model

data class ExecucaoRegra(
    val idExecucaoRegra: Int?,
    val execucao: Execucao?,
    val regra: Regra?,
    val respostas: List<ExecucaoRegraResposta>,
    val validou: Boolean?
) {

    constructor(): this(null,
        null, null, emptyList(), null)
}