package com.ry05k2ulv.oredrums.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ry05k2ulv.oredrums.data.DrumsRepository
import com.ry05k2ulv.oredrums.data.UserDataRepository
import com.ry05k2ulv.oredrums.model.DrumsProperty
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: DrumsRepository
) : ViewModel() {

    val uiState = repository.getPropertyList()
        .map { HomeUiState.Success(it) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, HomeUiState.Loading)

    fun insertProperty(title: String) {
        viewModelScope.launch {
            repository.insertProperty(title)
        }
    }

    fun deletePropertyById(id: Int) {
        viewModelScope.launch {
            repository.deletePropertyById(id)
        }
    }

    fun updateProperty(drumsProperty: DrumsProperty) {
        viewModelScope.launch {
            repository.updateProperty(drumsProperty)
        }
    }
}

sealed interface HomeUiState {
    object Loading : HomeUiState
    data class Success(val properties: List<DrumsProperty>) : HomeUiState
}