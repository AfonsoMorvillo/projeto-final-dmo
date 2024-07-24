package br.edu.ifsp.arq.ads.dmo.model

import java.io.Serializable
import java.util.UUID

data class Grupo(
    var id: String = UUID.randomUUID().toString(),
    var userId: String = "",
    var nome: String = "",
    var descricao: String = "",
    var dataFinal: String = "",
    var tipoMaterial: TipoMaterial? = TipoMaterial.LATA,
    var memberIds: List<String> = emptyList()
) : Serializable {

    constructor() : this(
        UUID.randomUUID().toString(),
        "",
        "",
        "",
        "",
        TipoMaterial.LATA,
        emptyList()
    )

    enum class TipoMaterial(val value: String) {
        LATA("Lata"),
        PAPEL("Papel"),
        VIDRO("Vidro"),
        LACRE("Lacre")
    }
}
