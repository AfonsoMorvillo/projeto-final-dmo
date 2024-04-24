package br.edu.ifsp.arq.ads.dmo.database

import androidx.room.TypeConverter
import br.edu.ifsp.arq.ads.dmo.model.User
import java.time.LocalDate

class Converters {

    @TypeConverter
    fun fromTimestamp(value: String?): LocalDate? {
        return value?.let { LocalDate.parse(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDate?): String? {
        return date?.toString()
    }
}