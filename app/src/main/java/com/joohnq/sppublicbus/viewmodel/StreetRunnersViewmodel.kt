package com.joohnq.sppublicbus.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joohnq.sppublicbus.common.state.UiState
import com.joohnq.sppublicbus.model.entity.StreetRunner
import com.joohnq.sppublicbus.model.repository.StreetRunnersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StreetRunnersViewmodel @Inject constructor(
    private val repository: StreetRunnersRepository,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _streetRunners = MutableLiveData<UiState<List<StreetRunner>>>()
    val streetRunners: LiveData<UiState<List<StreetRunner>>> get() = _streetRunners

    fun getStreetRunners() {
        viewModelScope.launch(dispatcher) {
            _streetRunners.manage(
                repository
                    .getStreetRunners()
            )
        }
    }
}