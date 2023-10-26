package com.ry05k2ulv.oredrums.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.ry05k2ulv.oredrums.datastore.UserPreferences
import com.ry05k2ulv.oredrums.datastore.UserPreferencesSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Provides
    fun providesDataSource(
        @ApplicationContext context: Context,
        serializer: UserPreferencesSerializer
    ): DataStore<UserPreferences> = DataStoreFactory.create(
        serializer
    ) {
        context.dataStoreFile("user_preferences.pb")
    }
}