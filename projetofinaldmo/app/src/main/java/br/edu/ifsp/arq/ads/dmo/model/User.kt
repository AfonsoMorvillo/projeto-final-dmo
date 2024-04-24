package br.edu.ifsp.arq.ads.dmo.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.time.LocalDate
import java.util.UUID

@Entity(tableName = "user")
data class User (
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val email: String,
    val name: String,
    val password: String,
    val image: String,
    val dateOfBirth: LocalDate?
)