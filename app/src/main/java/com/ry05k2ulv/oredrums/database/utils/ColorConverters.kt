package com.ry05k2ulv.oredrums.database.utils

import androidx.compose.ui.graphics.Color
import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter

@ProvidedTypeConverter
class ColorConverters {
    @TypeConverter
    fun fromColor(color: Color): ULong {
        return color.value
    }

    @TypeConverter
    fun toColor(uLong: ULong): Color {
        return Color(uLong)
    }
}