package com.joohnq.sppublicbus.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joohnq.sppublicbus.common.state.UiState
import com.joohnq.sppublicbus.model.repository.AuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewmodel @Inject constructor(
    private val repository: AuthenticationRepository,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _authentication = MutableLiveData<UiState<Boolean>>()
    val authentication: LiveData<UiState<Boolean>> get() = _authentication

    fun authentication() {
        viewModelScope.launch(dispatcher) {
            _authentication.manage(
                repository
                    .authentication()
            )
        }
    }
}