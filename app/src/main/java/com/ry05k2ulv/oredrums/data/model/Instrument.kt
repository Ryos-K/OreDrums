package com.ry05k2ulv.oredrums.data.model

import com.ry05k2ulv.oredrums.database.model.InstrumentEntity
import com.ry05k2ulv.oredrums.model.Instrument

fun Instrument.asEntity() = InstrumentEntity(
    id, name, soundUri, imageUri, color, createdAt, updatedAt
)

fun InstrumentEntity.asExternalModel() = Instrument(
    id, name, soundUri, imageUri, color, createdAt, updatedAt
)