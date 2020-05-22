package com.yuri.luis.garcia.pereira.tcs_implementacao.model

data class Image(
    val idImage: Int?,
    val nome: String?,
    val extensao: String?,
    val contornos: Int?,
    val manchas: Int?,
    val image: ByteArray?,
    val imageProc: String?
) {

    constructor():this( null, null, null, null, null, null, null)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Image

        if (idImage != other.idImage) return false
        if (nome != other.nome) return false
        if (extensao != other.extensao) return false
        if (contornos != other.contornos) return false
        if (manchas != other.manchas) return false
        if (image != null) {
            if (other.image == null) return false
            if (!image.contentEquals(other.image)) return false
        } else if (other.image != null) return false
        if (imageProc != other.imageProc) return false

        return true
    }

    override fun hashCode(): Int {
        var result = idImage ?: 0
        result = 31 * result + (nome?.hashCode() ?: 0)
        result = 31 * result + (extensao?.hashCode() ?: 0)
        result = 31 * result + (contornos ?: 0)
        result = 31 * result + (manchas ?: 0)
        result = 31 * result + (image?.contentHashCode() ?: 0)
        result = 31 * result + (imageProc?.hashCode() ?: 0)
        return result
    }
}