package com.ry05k2ulv.oredrums.database.utils

import android.net.Uri
import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter

@ProvidedTypeConverter
class UriConverters {
    @TypeConverter
    fun fromUri(uri: Uri): String {
        return uri.toString()
    }

    @TypeConverter
    fun toUri(string: String): Uri {
        return Uri.parse(string)
    }

    @TypeConverter
    fun fromUri(uri: Uri?): String? {
        return uri?.let { it.toString() }
    }

    @TypeConverter
    fun toUri(string: String?): Uri? {
        return string?.let { Uri.parse(it) }
    }
}