package com.ry05k2ulv.oredrums.di

import android.content.Context
import androidx.room.Room
import com.ry05k2ulv.oredrums.database.OreDatabase
import com.ry05k2ulv.oredrums.database.utils.ColorConverters
import com.ry05k2ulv.oredrums.database.utils.DateConverters
import com.ry05k2ulv.oredrums.database.utils.UriConverters
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun providesOreDatabase(
        @ApplicationContext context: Context
    ): OreDatabase = Room.databaseBuilder(
        context = context,
        klass = OreDatabase::class.java,
        name = "ore_database"
    )
        .addTypeConverter(ColorConverters())
        .addTypeConverter(DateConverters())
        .addTypeConverter(UriConverters())
        .fallbackToDestructiveMigration()
        .build()
}