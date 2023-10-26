package com.ry05k2ulv.oredrums.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import java.time.LocalDateTime
import java.util.Date

internal const val DRUMS_PROPERTY_TABLE = "drums_properties"
internal const val DRUMS_PROPERTY_ID = "drums_property_id"

@Entity(tableName = DRUMS_PROPERTY_TABLE)
data class DrumsPropertyEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = DRUMS_PROPERTY_ID) val id: Int,
    val title: String,
    @ColumnInfo(name = "created_at") val createdAt: LocalDateTime,
    @ColumnInfo(name = "updated_at") val updatedAt: LocalDateTime
)