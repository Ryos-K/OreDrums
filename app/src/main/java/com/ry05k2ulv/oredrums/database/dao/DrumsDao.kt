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
import com.ry05k2ulv.oredrums.database.model.DrumsPropertyWithDrumpads
import com.ry05k2ulv.oredrums.model.Drumpad
import kotlinx.coroutines.flow.Flow

@Dao
interface DrumsDao {
    @Transaction
    @Query("select * from $DRUMS_PROPERTY_TABLE where $DRUMS_PROPERTY_ID = :id")
    fun getDrumsById(id: Int): Flow<DrumsPropertyWithDrumpads>

    @Query("select * from $DRUMPAD_TABLE")
    fun getAllDrumpads(): Flow<List<DrumpadEntity>>

    @Upsert
    suspend fun upsertDrumpad(drumpad: DrumpadEntity)

    @Query("delete from $DRUMPAD_TABLE where $DRUMPAD_ID = :id")
    suspend fun deleteDrumpadById(id: Int)

}