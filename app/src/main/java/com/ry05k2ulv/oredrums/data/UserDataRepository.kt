package com.ry05k2ulv.oredrums.data

import com.ry05k2ulv.oredrums.datastore.OrePreferencesDataSource
import com.ry05k2ulv.oredrums.model.DarkThemeConfig
import com.ry05k2ulv.oredrums.model.UserData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserDataRepository @Inject constructor(
    private val dataSource: OrePreferencesDataSource
) {
    val userData: Flow<UserData> = dataSource.userData

    suspend fun setUseDynamicColor(useDynamicColor: Boolean) {
        dataSource.setUseDynamicColor(useDynamicColor)
    }

    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        dataSource.setDarkThemeConfig(darkThemeConfig)
    }
}