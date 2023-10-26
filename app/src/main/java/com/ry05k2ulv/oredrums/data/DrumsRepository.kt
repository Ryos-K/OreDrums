package com.ry05k2ulv.oredrums.data

import com.ry05k2ulv.oredrums.data.model.asEntity
import com.ry05k2ulv.oredrums.data.model.asExternalModel
import com.ry05k2ulv.oredrums.database.dao.DrumsDao
import com.ry05k2ulv.oredrums.database.dao.DrumsPropertyDao
import com.ry05k2ulv.oredrums.database.dao.InstrumentDao
import com.ry05k2ulv.oredrums.model.Drumpad
import com.ry05k2ulv.oredrums.model.Drums
import com.ry05k2ulv.oredrums.model.DrumsProperty
import com.ry05k2ulv.oredrums.model.Instrument
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class DrumsRepository @Inject constructor(
    private val drumsDao: DrumsDao,
    private val drumsPropertyDao: DrumsPropertyDao,
    private val instrumentDao: InstrumentDao
) {
    /* -------------------------------
        Functions about DrumsProperty
       ------------------------------- */
    fun getPropertyList(): Flow<List<DrumsProperty>> =
        drumsPropertyDao.getAll().map { list -> list.map { it.asExternalModel() } }

    fun insertProperty(drumsProperty: DrumsProperty) {
        drumsPropertyDao.insert(drumsProperty.asEntity())
    }

    fun updateProperty(drumsProperty: DrumsProperty) {
        drumsPropertyDao.update(drumsProperty.asEntity())
    }

    fun deletePropertyById(id: Int) = drumsPropertyDao.deleteById(id)

    /* ----------------------
        Function about Drums
       ---------------------- */
    fun getDrumsById(id: Int): Flow<Drums> = drumsDao.getDrumsById(id).map { it.asExternalModel() }

    fun getDrumpadList(): Flow<List<Drumpad>> = drumsDao.getAllDrumpads().map { list -> list.map { it.asExternalModel() } }

    fun upsertDrumpad(drumpad: Drumpad) {
        drumsDao.upsertDrumpad(drumpad.asEntity())
    }

    fun deleteDrumpadById(id: Int) {
        drumsDao.deleteDrumpadById(id)
    }

    /* ----------------------------
        Functions about Instrument
       ---------------------------- */
    fun getInstrumentList(): Flow<List<Instrument>> =
        instrumentDao.getAll().map { list -> list.map { it.asExternalModel() } }

    fun insertInstrument(instrument: Instrument) {
        instrumentDao.insert(instrument.asEntity())
    }

    fun updateInstrument(instrument: Instrument) {
        instrumentDao.update(instrument.asEntity())
    }

    fun deleteInstrumentById(id: Int) {
        instrumentDao.deleteById(id)
    }


}