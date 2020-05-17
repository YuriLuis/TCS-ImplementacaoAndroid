package com.yuri.luis.garcia.pereira.tcs_implementacao.enuns

enum class TipoVariavel(private var tipo: Int) {

    TIPO_NUMERICA(0),
    TIPO_UNIVALORADA(1),
    MULTIVALORA(2),
    NAO_INFORMADO(3);

    fun TipoVariavel(tipo : Int){
        this.tipo = tipo
    }

    fun getTipo(): Int {
        return this.tipo
    }
}