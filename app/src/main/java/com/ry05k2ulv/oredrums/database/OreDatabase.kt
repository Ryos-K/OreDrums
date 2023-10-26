package com.ry05k2ulv.oredrums.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ry05k2ulv.oredrums.database.dao.DrumsDao
import com.ry05k2ulv.oredrums.database.dao.DrumsPropertyDao
import com.ry05k2ulv.oredrums.database.dao.InstrumentDao
import com.ry05k2ulv.oredrums.database.model.DrumpadEntity
import com.ry05k2ulv.oredrums.database.model.DrumsPropertyEntity
import com.ry05k2ulv.oredrums.database.model.InstrumentEntity
import com.ry05k2ulv.oredrums.database.utils.LocalDateTimeConverters
import com.ry05k2ulv.oredrums.database.utils.UriConverters

@Database(
    entities = [DrumsPropertyEntity::class, DrumpadEntity::class, InstrumentEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(LocalDateTimeConverters::class, UriConverters::class)
abstract class OreDatabase : RoomDatabase() {
    abstract fun drumsDao(): DrumsDao
    protected abstract fun rawDrumsPropertyDao(): DrumsPropertyDao
    fun drumsPropertyDao() = DrumsPropertyDao.Wrapper(rawDrumsPropertyDao())
    protected abstract fun rawInstrumentDao(): InstrumentDao
    fun instrumentDao() = InstrumentDao.Wrapper(rawInstrumentDao())
}