package com.joohnq.sppublicbus.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joohnq.sppublicbus.common.state.UiState
import com.joohnq.sppublicbus.model.entity.BusLinesRequestItem
import com.joohnq.sppublicbus.model.repository.BusLineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BusLinesViewmodel @Inject constructor(
    private val repository: BusLineRepository,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _busLinesSearch =
        MutableLiveData<UiState<List<BusLinesRequestItem>>>()
    val busLinesSearch: LiveData<UiState<List<BusLinesRequestItem>>> get() = _busLinesSearch

    fun getBusLines(search: String) =
        viewModelScope.launch(dispatcher) {
            _busLinesSearch.manage(repository
                .getBusLines(search)
            )
        }
}