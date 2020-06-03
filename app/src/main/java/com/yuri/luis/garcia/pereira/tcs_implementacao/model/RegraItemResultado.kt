package com.yuri.luis.garcia.pereira.tcs_implementacao.model

import java.io.Serializable

data class RegraItemResultado(var idRegraItemResultado: Integer?,
                     var variavel: Variavel?,
                     var variavelValor: VariavelValor?,
                     var fatorConfianca: Double,
                     var regra: Regra?
) : Serializable {
    constructor(): this(null, null, null, 0.0, null)
}