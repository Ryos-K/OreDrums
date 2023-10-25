package com.ry05k2ulv.oredrums.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ry05k2ulv.oredrums.database.model.DRUMS_PROPERTY_ID
import com.ry05k2ulv.oredrums.database.model.DRUMS_PROPERTY_TABLE
import com.ry05k2ulv.oredrums.database.model.DrumsPropertyEntity
import kotlinx.coroutines.flow.Flow
import java.util.Calendar
import java.util.Date

@Dao
interface DrumsPropertyDao {
    @Query("select * from $DRUMS_PROPERTY_TABLE")
    fun getAll(): Flow<List<DrumsPropertyEntity>>

    @Insert
    fun _insert(drumsPropertyEntity: DrumsPropertyEntity)

    fun insert(drumsPropertyEntity: DrumsPropertyEntity) {
        _insert(
            drumsPropertyEntity.copy(
                createdAt = Date(System.currentTimeMillis()),
                updatedAt = Date(System.currentTimeMillis())
            )
        )
    }

    @Update
    fun _update(drumsPropertyEntity: DrumsPropertyEntity)

    fun update(drumsPropertyEntity: DrumsPropertyEntity) {
        _update(
            drumsPropertyEntity.copy(
                updatedAt = Date(System.currentTimeMillis())
            )
        )
    }

    @Query("delete from $DRUMS_PROPERTY_TABLE where $DRUMS_PROPERTY_ID = :id")
    fun deleteById(id: Int)
}