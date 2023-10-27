package com.ry05k2ulv.oredrums.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ry05k2ulv.oredrums.database.model.INSTRUMENT_ID
import com.ry05k2ulv.oredrums.database.model.INSTRUMENT_TABLE
import com.ry05k2ulv.oredrums.database.model.InstrumentEntity
import kotlinx.coroutines.flow.Flow
import java.time.Clock
import java.time.LocalDateTime
import java.util.Date

@Dao
interface InstrumentDao {
    @Query("select * from $INSTRUMENT_TABLE")
    fun getAll(): Flow<List<InstrumentEntity>>

    @Insert
    suspend fun insert(instrument: InstrumentEntity)

    @Update
    suspend fun update(instrument: InstrumentEntity)


    @Query("delete from $INSTRUMENT_TABLE where $INSTRUMENT_ID = :id")
    suspend fun deleteById(id: Int)

    class Wrapper(private val dao: InstrumentDao, var clock: Clock = Clock.systemDefaultZone()) : InstrumentDao by dao {
        override suspend fun insert(
            instrument: InstrumentEntity
        ) {
            dao.insert(
                instrument.copy(
                    createdAt = LocalDateTime.now(clock),
                    updatedAt = LocalDateTime.now(clock)
                )
            )
        }

        override suspend fun update(
           instrument: InstrumentEntity,
        ) {
            dao.update(
                instrument.copy(
                    updatedAt = LocalDateTime.now(clock)
                )
            )
        }
    }
}