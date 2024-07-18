package com.joohnq.sppublicbus.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joohnq.sppublicbus.common.state.UiState
import com.joohnq.sppublicbus.model.entity.BusStop
import com.joohnq.sppublicbus.model.repository.StopsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StopsViewmodel @Inject constructor(
    private val repository: StopsRepository,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _busPointsSearch =
        MutableLiveData<UiState<List<BusStop>>>()
    val busPointsSearch: LiveData<UiState<List<BusStop>>> get() = _busPointsSearch

    fun setBusPointsSearchNone() {
        _busPointsSearch.value = UiState.None
    }

    fun getBusPoints(search: String) {
        viewModelScope.launch(dispatcher) {
            _busPointsSearch.manage(
                repository
                    .getBusPoints(search)
            )
        }
    }

    fun getStopsByRunner(id: Int) {
        viewModelScope.launch(dispatcher) {
            _busPointsSearch.manage(
                repository
                    .getStopsByRunner(id)
            )
        }
    }
}