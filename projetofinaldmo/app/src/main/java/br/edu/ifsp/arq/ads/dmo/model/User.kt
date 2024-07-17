package br.edu.ifsp.arq.ads.dmo.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable
import java.time.LocalDate
import java.util.UUID

@Entity(tableName = "user")
data class User (
    @PrimaryKey var id: String = UUID.randomUUID().toString(),
    var email: String,
    var name: String,
    var password: String,
    var image: String,
    var dateOfBirth: String
): Serializable{

    @Ignore
    constructor(): this("","","","","","")
}
