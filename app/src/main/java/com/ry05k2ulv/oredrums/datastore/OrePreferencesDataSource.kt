package com.ry05k2ulv.oredrums.datastore

import androidx.datastore.core.DataStore
import com.ry05k2ulv.oredrums.datastore.UserPreferences.DarkThemeConfigProto.*
import com.ry05k2ulv.oredrums.model.DarkThemeConfig
import com.ry05k2ulv.oredrums.model.UserData
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OrePreferencesDataSource @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>
) {
    val userData = userPreferences.data.map {
        UserData(
            useDynamicColor = it.useDynamicColor,
            darkThemeConfig = when (it.darkThemeConfig) {
                null, UNRECOGNIZED, SYSTEM -> DarkThemeConfig.SYSTEM
                LIGHT -> DarkThemeConfig.LIGHT
                DARK -> DarkThemeConfig.DARK
            }
        )
    }

    suspend fun setUseDynamicColor(useDynamicColor: Boolean) {
        userPreferences.updateData {
            it.toBuilder().setUseDynamicColor(useDynamicColor).build()
        }
    }

    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        userPreferences.updateData {
            it.toBuilder().setDarkThemeConfig(
                when (darkThemeConfig) {
                    DarkThemeConfig.SYSTEM -> SYSTEM
                    DarkThemeConfig.LIGHT -> LIGHT
                    DarkThemeConfig.DARK -> DARK
                }
            ).build()
        }
    }
}