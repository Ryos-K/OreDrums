package com.ry05k2ulv.oredrums.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.ry05k2ulv.oredrums.database.model.DRUMPAD_ID
import com.ry05k2ulv.oredrums.database.model.DRUMPAD_TABLE
import com.ry05k2ulv.oredrums.database.model.DRUMS_PROPERTY_ID
import com.ry05k2ulv.oredrums.database.model.DRUMS_PROPERTY_TABLE
import com.ry05k2ulv.oredrums.database.model.DrumpadEntity

@Dao
interface DrumsDao {
    @Transaction
    @Query("select * from $DRUMS_PROPERTY_TABLE where $DRUMS_PROPERTY_ID = :id")
    fun getById(id: Int)

    @Upsert
    fun upsertDrumpad(drumpad: DrumpadEntity)

    @Query("select * from $DRUMPAD_TABLE where $DRUMPAD_ID = :id")
    fun deleteDrumpadById(id: Int)
}