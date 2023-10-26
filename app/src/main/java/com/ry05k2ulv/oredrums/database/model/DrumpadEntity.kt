package com.ry05k2ulv.oredrums.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

internal const val DRUMPAD_TABLE = "drumpad"
internal const val DRUMPAD_ID = "drumpad_id"

@Entity(
    tableName = DRUMPAD_TABLE,
    foreignKeys = [
        ForeignKey(
            entity = InstrumentEntity::class,
            parentColumns = [INSTRUMENT_ID],
            childColumns = [INSTRUMENT_ID],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = DrumsPropertyEntity::class,
            parentColumns = [DRUMS_PROPERTY_ID],
            childColumns = [DRUMS_PROPERTY_ID],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(DRUMS_PROPERTY_ID), Index(INSTRUMENT_ID)]
)
data class DrumpadEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = DRUMPAD_ID) val id: Int,
    @ColumnInfo(name = DRUMS_PROPERTY_ID) val drumsPropertyId: Int,
    @ColumnInfo(name = INSTRUMENT_ID) val instrumentId: Int,
    val x: Int,
    val y: Int,
    val width: Int,
    val height: Int,
)