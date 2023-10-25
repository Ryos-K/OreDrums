package com.ry05k2ulv.oredrums.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ry05k2ulv.oredrums.database.model.INSTRUMENT_ID
import com.ry05k2ulv.oredrums.database.model.INSTRUMENT_TABLE
import com.ry05k2ulv.oredrums.database.model.InstrumentEntity
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface InstrumentDao {
    @Query("select * from $INSTRUMENT_TABLE")
    fun getAll(): Flow<List<InstrumentEntity>>

    @Insert
    fun _insert(instrument: InstrumentEntity)

    fun insert(instrument: InstrumentEntity) {
        _insert(
            instrument.copy(
                createdAt = Date(System.currentTimeMillis()),
                updatedAt = Date(System.currentTimeMillis())
            )
        )
    }

    @Update
    fun _update(instrument: InstrumentEntity)

    fun update(instrument: InstrumentEntity) {
        _update(
            instrument.copy(
                updatedAt = Date(System.currentTimeMillis())
            )
        )
    }

    @Query("delete from $INSTRUMENT_TABLE where $INSTRUMENT_ID = :id")
    fun deleteById(id: Int)
}