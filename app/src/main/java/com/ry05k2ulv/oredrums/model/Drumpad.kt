package com.ry05k2ulv.oredrums.model

data class Drumpad(
    val id: Int,
    val drumsPropertyId: Int,
    val instrumentId: Int,
    val x: Int,
    val y: Int,
    val width: Int,
    val height: Int,
)
