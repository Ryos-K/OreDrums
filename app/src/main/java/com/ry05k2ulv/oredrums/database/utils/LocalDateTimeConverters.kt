package com.ry05k2ulv.oredrums.database.utils

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset


@ProvidedTypeConverter
class LocalDateTimeConverters {
    @TypeConverter
    fun fromLocalDateTime(localDateTime: LocalDateTime): String {
        return localDateTime.toString()
    }

    @TypeConverter
    fun toInstant(string: String): LocalDateTime {
        return LocalDateTime.parse(string)
    }
}