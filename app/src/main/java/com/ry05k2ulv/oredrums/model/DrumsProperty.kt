package com.ry05k2ulv.oredrums.model

import java.time.Instant
import java.time.LocalDateTime
import java.util.Date

data class DrumsProperty(
    val id: Int,
    val title: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)