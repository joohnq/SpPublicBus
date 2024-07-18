package com.joohnq.sppublicbus.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joohnq.sppublicbus.common.state.UiState
import com.joohnq.sppublicbus.model.entity.PositionBusByLinesRequest
import com.joohnq.sppublicbus.model.repository.PositionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PositionViewmodel @Inject constructor(
    private val repository: PositionRepository,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _currentBusLineId = MutableStateFlow(0)
    private val currentBusLineId: StateFlow<Int> get() = _currentBusLineId

    private val _positionVehicles = MutableLiveData<UiState<PositionBusByLinesRequest>>()
    val positionVehicles: LiveData<UiState<PositionBusByLinesRequest>> get() = _positionVehicles

    fun setPositionVehicles() {
        _positionVehicles.value = UiState.None
    }

    fun getPositionVehiclesByBusLines(id: Int = currentBusLineId.value) {
        viewModelScope.launch(dispatcher) {
            _currentBusLineId.value = id
            _positionVehicles.manage(
                repository
                    .getPositionVehiclesByBusLines(id)
            )
        }
    }
}