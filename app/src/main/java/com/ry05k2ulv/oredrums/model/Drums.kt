package com.ry05k2ulv.oredrums.model

data class Drums(
    val drumsProperty: DrumsProperty,
    val drumpadWithInstrumentList: List<Pair<Drumpad, Instrument>>
)

