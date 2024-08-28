package br.edu.ifsp.arq.ads.dmo.dto

import br.edu.ifsp.arq.ads.dmo.model.Postagem
import com.google.firebase.Timestamp
import java.io.Serializable
import java.util.UUID

data class PostagemDTO(
    var postagem: Postagem,
    var fotoUser: String = "",
) : Serializable {

    constructor() : this(
         Postagem(),
        ""
    )
}
