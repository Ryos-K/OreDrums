package com.ry05k2ulv.oredrums.data

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.net.Uri
import androidx.compose.ui.graphics.Color
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.ry05k2ulv.oredrums.database.OreDatabase
import com.ry05k2ulv.oredrums.database.dao.DrumsDao
import com.ry05k2ulv.oredrums.database.dao.DrumsPropertyDao
import com.ry05k2ulv.oredrums.database.dao.InstrumentDao
import com.ry05k2ulv.oredrums.database.utils.LocalDateTimeConverters
import com.ry05k2ulv.oredrums.database.utils.UriConverters
import com.ry05k2ulv.oredrums.model.Drumpad
import com.ry05k2ulv.oredrums.model.Drums
import com.ry05k2ulv.oredrums.model.DrumsProperty
import com.ry05k2ulv.oredrums.model.Instrument
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.Clock
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId


@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class DrumsRepositoryTest {

    private val testScope = TestScope(StandardTestDispatcher())

    private val context = ApplicationProvider.getApplicationContext<Context>()

    private lateinit var subject: DrumsRepository

    private lateinit var database: OreDatabase

    private lateinit var drumsDao: DrumsDao

    private lateinit var drumsPropertyDao: DrumsPropertyDao.Wrapper

    private lateinit var instrumentDao: InstrumentDao.Wrapper

    private val propertyDummy = listOf(
        DrumsProperty(1, "Drums 1", LocalDateTime.MIN, LocalDateTime.MIN),
        DrumsProperty(2, "Drums 2", LocalDateTime.MIN, LocalDateTime.MIN)
    )

    private val instrumentDummy = listOf(
        Instrument(
            1,
            "boo",
            Uri.parse("http://boo/audio"),
            Uri.parse("http://boo/image"),
            Color.Black,
            LocalDateTime.MIN,
            LocalDateTime.MIN
        ),
        Instrument(
            2,
            "foo",
            Uri.parse("http://foo/audio"),
            Uri.parse("http://foo/image"),
            Color.White,
            LocalDateTime.MIN,
            LocalDateTime.MIN
        ),
    )

    private val drumpadDummy = listOf(
        Drumpad(1, 1, 1, 100, 120, 30, 42),
        Drumpad(2, 1, 2, 50, 70, 100, 140),
        Drumpad(3, 2, 1, 10, 20, 45, 150)
    )

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            context,
            OreDatabase::class.java
        )
            .addTypeConverter(LocalDateTimeConverters())
            .addTypeConverter(UriConverters())
            .fallbackToDestructiveMigration()
            .build()

        drumsDao = database.drumsDao()
        drumsPropertyDao = database.drumsPropertyDao()
        instrumentDao = database.instrumentDao()

        subject = DrumsRepository(
            drumsDao, drumsPropertyDao, instrumentDao
        )
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun return_empty_property_list_at_first_time() {
        testScope.runTest {
            assertThat(subject.getPropertyList().first()).isEmpty()
        }
    }

    @Test
    fun return_property_list_overwritten_createdAt_and_updatedAt_on_insertion() {
        testScope.runTest {
            val clock1 = Clock.fixed(Instant.now(), ZoneId.systemDefault())
            val dateTime1 = LocalDateTime.now(clock1)

            drumsPropertyDao.clock = clock1
            subject.insertProperty(propertyDummy[0])
            subject.insertProperty(propertyDummy[1])

            assertThat(subject.getPropertyList().first())
                .containsExactly(
                    propertyDummy[0].copy(createdAt = dateTime1, updatedAt = dateTime1),
                    propertyDummy[1].copy(createdAt = dateTime1, updatedAt = dateTime1)
                )
        }
    }

    @Test
    fun return_property_list_overwritten_updatedAt_on_update() {
        testScope.runTest {
            val clock1 = Clock.fixed(Instant.now(), ZoneId.systemDefault())
            val clock2 = Clock.offset(clock1, Duration.ofDays(1))
            val dateTime1 = LocalDateTime.now(clock1)
            val dateTime2 = LocalDateTime.now(clock2)

            drumsPropertyDao.clock = clock1
            subject.insertProperty(propertyDummy[0])
            subject.insertProperty(propertyDummy[1])

            drumsPropertyDao.clock = clock2
            subject.updateProperty(
                subject.getPropertyList().first().first().copy(
                    title = "New Drums 1",
                )
            )

            assertThat(subject.getPropertyList().first())
                .containsExactly(
                    propertyDummy[0].copy(
                        title = "New Drums 1", createdAt = dateTime1, updatedAt = dateTime2
                    ),
                    propertyDummy[1].copy(createdAt = dateTime1, updatedAt = dateTime1)
                )
        }
    }

    @Test
    fun delete_property_by_id() {
        testScope.runTest {
            val clock1 = Clock.fixed(Instant.now(), ZoneId.systemDefault())
            val dateTime1 = LocalDateTime.now(clock1)

            drumsPropertyDao.clock = clock1
            subject.insertProperty(propertyDummy[0])
            subject.insertProperty(propertyDummy[1])
            subject.deletePropertyById(1)

            assertThat(subject.getPropertyList().first()).containsExactly(
                propertyDummy[1].copy(createdAt = dateTime1, updatedAt = dateTime1)
            )
        }
    }

    @Test
    fun get_drums_joined_drumpad_and_instrument_by_id() {
        testScope.runTest {
            val clock1 = Clock.fixed(Instant.now(), ZoneId.systemDefault())
            val dateTime1 = LocalDateTime.now(clock1)

            drumsPropertyDao.clock = clock1
            instrumentDao.clock = clock1
            propertyDummy.forEach { subject.insertProperty(it) }
            instrumentDummy.forEach { subject.insertInstrument(it) }
            drumpadDummy.forEach { subject.upsertDrumpad(it) }

            assertThat(subject.getDrumsById(1).first()).isEqualTo(
                Drums(
                    propertyDummy[0].copy(createdAt = dateTime1, updatedAt = dateTime1),
                    listOf(
                        drumpadDummy[0] to instrumentDummy[0].copy(
                            createdAt = dateTime1,
                            updatedAt = dateTime1
                        ),
                        drumpadDummy[1] to instrumentDummy[1].copy(
                            createdAt = dateTime1,
                            updatedAt = dateTime1
                        )
                    )
                )
            )
            assertThat(subject.getDrumsById(2).first()).isEqualTo(
                Drums(
                    propertyDummy[1].copy(createdAt = dateTime1, updatedAt = dateTime1),
                    listOf(
                        drumpadDummy[2] to instrumentDummy[0].copy(
                            createdAt = dateTime1,
                            updatedAt = dateTime1
                        )
                    )
                )
            )
        }
    }

    @Test
    fun upsert_drumpad() {
        testScope.runTest {
            propertyDummy.forEach { subject.insertProperty(it) }
            instrumentDummy.forEach { subject.insertInstrument(it) }
            drumpadDummy.forEach { subject.upsertDrumpad(it) }

            assertThat(subject.getDrumpadList().first()).isEqualTo(drumpadDummy)

            subject.upsertDrumpad(drumpadDummy[1].copy(x = 200, y = 300))
            subject.upsertDrumpad(drumpadDummy[2].copy(width = 400, height = 500))

            assertThat(subject.getDrumpadList().first()).isEqualTo(
                listOf(
                    drumpadDummy[0],
                    drumpadDummy[1].copy(x = 200, y = 300),
                    drumpadDummy[2].copy(width = 400, height = 500),
                )
            )
        }
    }

    @Test
    fun delete_drumpad_by_id() {
        testScope.runTest {
            propertyDummy.forEach { subject.insertProperty(it) }
            instrumentDummy.forEach { subject.insertInstrument(it) }
            drumpadDummy.forEach { subject.upsertDrumpad(it) }

            subject.deleteDrumpadById(2)

            assertThat(subject.getDrumpadList().first()).isEqualTo(
                listOf(
                    drumpadDummy[0],
                    drumpadDummy[2]
                )
            )
        }
    }

    @Test
    fun cascade_deletion_of_foreign_key_to_property() {
        testScope.runTest {
            propertyDummy.forEach { subject.insertProperty(it) }
            instrumentDummy.forEach { subject.insertInstrument(it) }
            drumpadDummy.forEach { subject.upsertDrumpad(it) }

            subject.deletePropertyById(1)

            assertThat(subject.getDrumpadList().first()).isEqualTo(
                listOf(drumpadDummy[2])
            )
        }
    }

    @Test
    fun cascade_deletion_of_foreign_key_to_instrument() {
        testScope.runTest {
            propertyDummy.forEach { subject.insertProperty(it) }
            instrumentDummy.forEach { subject.insertInstrument(it) }
            drumpadDummy.forEach { subject.upsertDrumpad(it) }

            subject.deleteInstrumentById(1)

            assertThat(subject.getDrumpadList().first()).isEqualTo(
                listOf(drumpadDummy[1])
            )
        }
    }

    @Test(expected = SQLiteConstraintException::class)
    fun throw_exception_when_foreign_key_to_property_is_not_exist() {
        testScope.runTest {
            instrumentDummy.forEach { subject.insertInstrument(it) }
            drumpadDummy.forEach { subject.upsertDrumpad(it) }
        }
    }

    @Test(expected = SQLiteConstraintException::class)
    fun throw_exception_when_foreign_key_to_instrument_is_not_exist() {
        testScope.runTest {
            propertyDummy.forEach { subject.insertProperty(it) }
            drumpadDummy.forEach { subject.upsertDrumpad(it) }
        }
    }

    @Test
    fun get_empty_instrument_list_at_first() {
        testScope.runTest {
            assertThat(subject.getInstrumentList().first()).isEmpty()
        }
    }

    @Test
    fun return_instrument_list_overwritten_createdAt_and_updatedAt_on_insertion() {
        testScope.runTest {
            val clock1 = Clock.fixed(Instant.now(), ZoneId.systemDefault())
            val dateTime1 = LocalDateTime.now(clock1)

            instrumentDao.clock = clock1
            subject.insertInstrument(instrumentDummy[0])
            subject.insertInstrument(instrumentDummy[1])

            assertThat(subject.getInstrumentList().first())
                .containsExactly(
                    instrumentDummy[0].copy(createdAt = dateTime1, updatedAt = dateTime1),
                    instrumentDummy[1].copy(createdAt = dateTime1, updatedAt = dateTime1)
                )
        }
    }

    @Test
    fun return_instrument_list_overwritten_updatedAt_on_update() {
        testScope.runTest {
            val clock1 = Clock.fixed(Instant.now(), ZoneId.systemDefault())
            val clock2 = Clock.offset(clock1, Duration.ofDays(1))
            val dateTime1 = LocalDateTime.now(clock1)
            val dateTime2 = LocalDateTime.now(clock2)

            instrumentDao.clock = clock1
            subject.insertInstrument(instrumentDummy[0])
            subject.insertInstrument(instrumentDummy[1])

            instrumentDao.clock = clock2
            subject.updateInstrument(
                subject.getInstrumentList().first().first().copy(
                    color = Color.Red
                )
            )

            assertThat(subject.getInstrumentList().first())
                .containsExactly(
                    instrumentDummy[0].copy(
                        color = Color.Red, createdAt = dateTime1, updatedAt = dateTime2
                    ),
                    instrumentDummy[1].copy(createdAt = dateTime1, updatedAt = dateTime1)
                )
        }
    }

    @Test
    fun delete_instrument_by_id() {
        testScope.runTest {
            val clock1 = Clock.fixed(Instant.now(), ZoneId.systemDefault())
            val dateTime1 = LocalDateTime.now(clock1)

            instrumentDao.clock = clock1
            subject.insertInstrument(instrumentDummy[0])
            subject.insertInstrument(instrumentDummy[1])
            subject.deleteInstrumentById(1)

            assertThat(subject.getInstrumentList().first()).containsExactly(
                instrumentDummy[1].copy(createdAt = dateTime1, updatedAt = dateTime1)
            )
        }
    }
}