package com.ry05k2ulv.oredrums.data.model

import com.ry05k2ulv.oredrums.database.model.DrumpadEntity
import com.ry05k2ulv.oredrums.model.Drumpad

fun Drumpad.asEntity() = DrumpadEntity(
    id, drumsPropertyId, instrumentId, x, y, width, height
)

fun DrumpadEntity.asExternalModel() = Drumpad(
    id, drumsPropertyId, instrumentId, x, y, width, height
)