package com.ry05k2ulv.oredrums.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ry05k2ulv.oredrums.database.model.DRUMS_PROPERTY_ID
import com.ry05k2ulv.oredrums.database.model.DRUMS_PROPERTY_TABLE
import com.ry05k2ulv.oredrums.database.model.DrumsPropertyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DrumsPropertyDao {
    @Query("select * from $DRUMS_PROPERTY_TABLE")
    fun getAll(): Flow<List<DrumsPropertyEntity>>

    @Insert
    fun insert(drumsPropertyEntity: DrumsPropertyEntity)

    @Update
    fun update(drumsPropertyEntity: DrumsPropertyEntity)

    @Query("delete from $DRUMS_PROPERTY_TABLE where $DRUMS_PROPERTY_ID = :id")
    fun deleteById(id: Int)
}