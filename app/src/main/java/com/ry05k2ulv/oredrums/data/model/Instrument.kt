package com.ry05k2ulv.oredrums.data.model

import androidx.compose.ui.graphics.Color
import com.ry05k2ulv.oredrums.database.model.InstrumentEntity
import com.ry05k2ulv.oredrums.model.Instrument

fun Instrument.asEntity() = InstrumentEntity(
    id, name, soundUri, imageUri, color.value.toLong(), createdAt, updatedAt
)

fun InstrumentEntity.asExternalModel() = Instrument(
    id, name, soundUri, imageUri, Color(color.toULong()), createdAt, updatedAt
)