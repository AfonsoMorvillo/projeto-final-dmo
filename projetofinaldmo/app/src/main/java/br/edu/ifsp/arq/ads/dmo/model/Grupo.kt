package br.edu.ifsp.arq.ads.dmo.model

import java.io.Serializable
import java.util.UUID

data class Grupo(
    var id: String = UUID.randomUUID().toString(),
    var userId: String = "",
    var nome: String = "",
    var descricao: String = "",
    var dataFinal: String = "",
    var foto: String = "",
    var tipoMaterial: TipoMaterial? = TipoMaterial.LATA,
    var memberIds: List<String> = emptyList(),
    var meta: Int? = 0,
    var quantidadeAtual: Int? =0
) : Serializable {

    constructor() : this(
        UUID.randomUUID().toString(),
        "",
        "",
        "",
        "",
        "",
        TipoMaterial.LATA,
        emptyList(),
        0,
        0
    )

    enum class TipoMaterial(val value: String) {
        LATA("Lata"),
        LACRE("Lacre"),
        GARRAFA("Garrafa PET"),
        TAMPINHA("Tampinha"),
        PILHA("Pilha"),
        LAMPADA("Lâmpada")

    }

    fun calculaPercentual(): Int {
        val percentualProgresso = if (this.meta!!.compareTo(0) > 0) {
            val calculoPercentual = (this.quantidadeAtual!!.toFloat() / this.meta!!.toFloat()) * 100
            calculoPercentual.coerceAtMost(100f)
        } else {
            0f
        }

        return percentualProgresso.toInt()
    }
}
