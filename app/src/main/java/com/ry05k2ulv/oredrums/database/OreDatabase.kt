package com.ry05k2ulv.oredrums.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ry05k2ulv.oredrums.database.dao.DrumsDao
import com.ry05k2ulv.oredrums.database.dao.DrumsPropertyDao
import com.ry05k2ulv.oredrums.database.dao.InstrumentDao
import com.ry05k2ulv.oredrums.database.model.DrumpadEntity
import com.ry05k2ulv.oredrums.database.model.DrumsEntity
import com.ry05k2ulv.oredrums.database.model.DrumsPropertyEntity
import com.ry05k2ulv.oredrums.database.model.InstrumentEntity
import com.ry05k2ulv.oredrums.database.utils.ColorConverters
import com.ry05k2ulv.oredrums.database.utils.DateConverters
import com.ry05k2ulv.oredrums.database.utils.UriConverters

@Database(
    entities = [DrumsEntity::class, DrumsPropertyEntity::class, DrumpadEntity::class, InstrumentEntity::class],
    version = 1
)
@TypeConverters(ColorConverters::class, DateConverters::class, UriConverters::class)
abstract class OreDatabase : RoomDatabase() {
    abstract fun drumsDao(): DrumsDao
    abstract fun drumsPropertyDao(): DrumsPropertyDao
    abstract fun instrumentDao(): InstrumentDao
}