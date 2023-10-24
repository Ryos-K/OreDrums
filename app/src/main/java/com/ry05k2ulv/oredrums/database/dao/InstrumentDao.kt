package com.ry05k2ulv.oredrums.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ry05k2ulv.oredrums.database.model.INSTRUMENT_ID
import com.ry05k2ulv.oredrums.database.model.INSTRUMENT_TABLE
import com.ry05k2ulv.oredrums.database.model.InstrumentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InstrumentDao {
    @Query("select * from $INSTRUMENT_TABLE")
    fun getAll(): Flow<List<InstrumentEntity>>

    @Insert
    fun insert(instrument: InstrumentEntity)

    @Update
    fun update(instrument: InstrumentEntity)

    @Query("delete from $INSTRUMENT_TABLE where $INSTRUMENT_ID = :id")
    fun deleteById(id: Int)
}