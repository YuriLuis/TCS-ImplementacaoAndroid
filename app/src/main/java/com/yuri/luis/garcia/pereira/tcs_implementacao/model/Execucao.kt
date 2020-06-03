package com.yuri.luis.garcia.pereira.tcs_implementacao.model

data class Execucao(
    val idExecucao: Int?,
    val datahora: String?,
    val tipoVariavel: Int?,
    val regras: List<ExecucaoRegra>,
    val image: Foto?,
    val concluido: String?,
    val percentualAcerto :Double?
) {
    constructor(): this(null,
        null, null, emptyList(), null,null,null)
}
