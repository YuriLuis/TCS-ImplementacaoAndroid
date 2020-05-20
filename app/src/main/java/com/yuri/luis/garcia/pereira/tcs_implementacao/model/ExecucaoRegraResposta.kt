package com.yuri.luis.garcia.pereira.tcs_implementacao.model

data class ExecucaoRegraResposta(
    val idExecucaoRegraResposta: Int?,
    val regraItem: RegraItem?,
    val execucaoRegra: ExecucaoRegra?,
    val resposta: VariavelValor?,
    val acertou: Boolean?
) {

    constructor(): this(null,
        null, null, null, null)
}