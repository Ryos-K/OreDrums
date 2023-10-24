package com.ry05k2ulv.oredrums.database.model

import android.net.Uri
import androidx.compose.ui.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

internal const val INSTRUMENT_TABLE = "instruments"
internal const val INSTRUMENT_ID = "instrument_id"

@Entity(tableName = INSTRUMENT_TABLE)
data class InstrumentEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = INSTRUMENT_ID) val id: Int,
    val name: String,
    @ColumnInfo(name = "sound_uri") val soundUri: Uri,
    @ColumnInfo(name = "image_uri") val imageUri: Uri?,
    val color: Color
)
