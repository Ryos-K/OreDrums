package com.ry05k2ulv.oredrums.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ry05k2ulv.oredrums.database.model.DRUMS_PROPERTY_ID
import com.ry05k2ulv.oredrums.database.model.DRUMS_PROPERTY_TABLE
import com.ry05k2ulv.oredrums.database.model.DrumsPropertyEntity
import kotlinx.coroutines.flow.Flow
import java.time.Clock
import java.time.LocalDateTime

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

    class Wrapper(private val dao: DrumsPropertyDao, var clock: Clock = Clock.systemDefaultZone()) :
        DrumsPropertyDao by dao {
        override fun insert(
            drumsPropertyEntity: DrumsPropertyEntity
        ) {
            dao.insert(
                drumsPropertyEntity.copy(
                    createdAt = LocalDateTime.now(clock),
                    updatedAt = LocalDateTime.now(clock)
                )
            )
        }

        override fun update(
            drumsPropertyEntity: DrumsPropertyEntity
        ) {
            dao.update(
                drumsPropertyEntity.copy(
                    updatedAt = LocalDateTime.now(clock)
                )
            )
        }
    }
}