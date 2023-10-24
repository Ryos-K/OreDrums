package com.ry05k2ulv.oredrums.database.model

import androidx.room.Embedded
import androidx.room.Relation

data class DrumsPropertyWithDrumpads(
    @Embedded val drumsProperty: DrumsPropertyEntity,
    @Relation(
        entity = DrumpadEntity::class,
        parentColumn = DRUMS_PROPERTY_ID,
        entityColumn = DRUMS_PROPERTY_ID
    ) val drumpadWithInstrumentList: List<DrumpadWithInstrument>
)

data class DrumpadWithInstrument(
    @Embedded val drumpad: DrumpadEntity,
    @Relation(
        parentColumn = DRUMPAD_ID,
        entityColumn = DRUMPAD_ID
    ) val instrument: InstrumentEntity
)