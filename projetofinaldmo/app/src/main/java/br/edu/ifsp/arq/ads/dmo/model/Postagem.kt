package br.edu.ifsp.arq.ads.dmo.model

import java.io.Serializable
import java.util.UUID

data class Postagem(
    var id: String = UUID.randomUUID().toString(),
    var userId: String = "",
    var nome: String = "",
    var descricao: String = "",
    var quantidade: String = "",
    var foto: String = "",
    var data: String = "",
) : Serializable {

    constructor() : this(
        UUID.randomUUID().toString(),
        "",
        "",
        "",
        "",
        "",
        ""
    )

}
