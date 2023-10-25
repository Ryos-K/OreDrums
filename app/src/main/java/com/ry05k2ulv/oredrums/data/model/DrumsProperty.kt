package com.ry05k2ulv.oredrums.data.model

import com.ry05k2ulv.oredrums.database.model.DrumsPropertyEntity
import com.ry05k2ulv.oredrums.model.DrumsProperty

fun DrumsProperty.asEntity() = DrumsPropertyEntity(
    id, title, createdAt, updatedAt
)

fun DrumsPropertyEntity.asExternalModel() = DrumsProperty(
    id, title, createdAt, updatedAt
)