package com.ry05k2ulv.oredrums.di

import com.ry05k2ulv.oredrums.database.OreDatabase
import com.ry05k2ulv.oredrums.database.dao.DrumsDao
import com.ry05k2ulv.oredrums.database.dao.DrumsPropertyDao
import com.ry05k2ulv.oredrums.database.dao.InstrumentDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {
    @Provides
    fun providesDrumsDao(
        database: OreDatabase
    ): DrumsDao = database.drumsDao()

    @Provides
    fun providesDrumsPropertyDao(
        database: OreDatabase
    ): DrumsPropertyDao = database.drumsPropertyDao()

    @Provides
    fun providesInstrumentDao(
        database: OreDatabase
    ): InstrumentDao = database.instrumentDao()
}