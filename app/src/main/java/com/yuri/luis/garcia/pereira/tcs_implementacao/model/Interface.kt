package com.yuri.luis.garcia.pereira.tcs_implementacao.model

import java.io.Serializable

data class Interface(var idInterface: Int?,
                     var variavel: Variavel?,
                     var pergunta: String
) : Serializable {
    constructor(): this(null, null, "")
}