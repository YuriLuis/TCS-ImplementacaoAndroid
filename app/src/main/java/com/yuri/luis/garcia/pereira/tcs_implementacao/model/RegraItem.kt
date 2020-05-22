package com.yuri.luis.garcia.pereira.tcs_implementacao.model

data class RegraItem(var idRegraItem: Int?,
                      var conectivo: Integer,
                     var variavel: Variavel,
                     var condicional: String,
                     var variavelValor: VariavelValor,
                     var pergunta: String,
                     var regra: Regra
) {

}