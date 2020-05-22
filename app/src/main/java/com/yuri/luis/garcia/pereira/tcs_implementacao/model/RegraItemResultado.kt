package com.yuri.luis.garcia.pereira.tcs_implementacao.model

data class RegraItemResultado(var idRegraItemResultado: Integer?,
                     var variavel: Variavel,
                     var variavelValor: VariavelValor,
                     var fatorConfianca: Double,
                     var regra: Regra
) {

}