package br.edu.ifsp.arq.ads.dmo.model

import com.google.firebase.Timestamp
import java.io.Serializable
import java.util.UUID

data class Postagem(
    var id: String = UUID.randomUUID().toString(),
    var userId: String = "",
    var nome: String = "",
    var descricao: String = "",
    var quantidade: Int? = 0,
    var foto: String = "",
    var data: Timestamp = Timestamp.now(),
    var grupoId: String = "",
    var nomeUsuario: String = ""
) : Serializable {

    constructor() : this(
        UUID.randomUUID().toString(),
        "",
        "",
        "",
        0,
        "",
        Timestamp.now(),
        "",
        ""
    )

}
