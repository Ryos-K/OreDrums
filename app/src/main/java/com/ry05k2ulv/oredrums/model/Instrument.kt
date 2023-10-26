package com.ry05k2ulv.oredrums.model

import android.net.Uri
import androidx.compose.ui.graphics.Color
import java.time.Instant
import java.time.LocalDateTime
import java.util.Date

data class Instrument(
    val id: Int,
    val name: String,
    val soundUri: Uri,
    val imageUri: Uri?,
    val color: Color,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
