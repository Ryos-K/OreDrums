package com.ry05k2ulv.oredrums.model

enum class DarkThemeConfig {
    SYSTEM,
    LIGHT,
    DARK
}
data class UserData(
    val useDynamicColor: Boolean,
    val darkThemeConfig: DarkThemeConfig
)