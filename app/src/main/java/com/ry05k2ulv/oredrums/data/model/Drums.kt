package com.ry05k2ulv.oredrums.data.model

import com.ry05k2ulv.oredrums.database.model.DrumsPropertyWithDrumpads
import com.ry05k2ulv.oredrums.model.Drums

fun DrumsPropertyWithDrumpads.asExternalModel() = Drums(
    drumsProperty.asExternalModel(),
    drumpadWithInstrumentList.map {
        Pair(
            it.drumpad.asExternalModel(),
            it.instrument.asExternalModel()
        )
    }
)