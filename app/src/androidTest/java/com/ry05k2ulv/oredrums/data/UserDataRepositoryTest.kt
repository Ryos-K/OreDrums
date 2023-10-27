package com.ry05k2ulv.oredrums.data

import android.content.Context
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import com.ry05k2ulv.oredrums.datastore.OrePreferencesDataSource
import com.ry05k2ulv.oredrums.datastore.UserPreferencesSerializer
import com.ry05k2ulv.oredrums.model.DarkThemeConfig
import com.ry05k2ulv.oredrums.model.UserData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

const val DATASTORE_FILENAME = "user_preferences_test.pb"

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class UserDataRepositoryTest {

    private val testScope = TestScope(StandardTestDispatcher())

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    private lateinit var subject: UserDataRepository

    @Before
    fun setUp() {
        subject = UserDataRepository(
            OrePreferencesDataSource(
                context.testUserPreferencesDataSource(testScope)
            )
        )
    }

    @After
    fun tearDown() {
        context.dataStoreFile(DATASTORE_FILENAME).deleteRecursively()
    }

    @Test
    fun return_default_value_at_first_time() {
        testScope.runTest {
            assertThat(subject.userData.first()).isEqualTo(
                UserData(
                    useDynamicColor = false,
                    darkThemeConfig = DarkThemeConfig.SYSTEM
                )
            )
        }
    }

    @Test
    fun return_setDynamicColor() {
        testScope.runTest {
            subject.setUseDynamicColor(false)
            assertThat(subject.userData.first().useDynamicColor).isFalse()

            subject.setUseDynamicColor(true)
            assertThat(subject.userData.first().useDynamicColor).isTrue()
        }
    }

    @Test
    fun return_setDarkThemeConfig() {
        testScope.runTest {
            subject.setDarkThemeConfig(DarkThemeConfig.SYSTEM)
            assertThat(subject.userData.first().darkThemeConfig)
                .isEqualTo(DarkThemeConfig.SYSTEM)

            subject.setDarkThemeConfig(DarkThemeConfig.LIGHT)
            assertThat(subject.userData.first().darkThemeConfig)
                .isEqualTo(DarkThemeConfig.LIGHT)

            subject.setDarkThemeConfig(DarkThemeConfig.DARK)
            assertThat(subject.userData.first().darkThemeConfig)
                .isEqualTo(DarkThemeConfig.DARK)
        }
    }
}

fun Context.testUserPreferencesDataSource(
    scope: CoroutineScope,
    serializer: UserPreferencesSerializer = UserPreferencesSerializer()
) = DataStoreFactory.create(
    serializer = serializer,
    scope = scope
) {
    dataStoreFile(DATASTORE_FILENAME)
}