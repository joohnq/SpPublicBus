package com.joohnq.sppublicbus.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joohnq.sppublicbus.common.state.UiState
import com.joohnq.sppublicbus.model.entity.ForecastStopRequest
import com.joohnq.sppublicbus.model.repository.ForecastRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForecastViewmodel @Inject constructor(
    private val repository: ForecastRepository,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _forecastByStop = MutableLiveData<UiState<ForecastStopRequest>>()
    val forecastByStop: LiveData<UiState<ForecastStopRequest>> get() = _forecastByStop

    fun getForecastByStop(id: Int) = viewModelScope.launch(dispatcher) {
        _forecastByStop.manage(
            repository.getForecastByStop(id)
        )
    }
}

