package com.joohnq.sppublicbus.viewmodel

import androidx.lifecycle.MutableLiveData
import com.joohnq.sppublicbus.common.state.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

suspend fun <T> MutableLiveData<UiState<T>>.manage(state: Flow<T>){
    postValue(UiState.Loading)
    state.catch {
        postValue(UiState.Error(it.message.toString()))
    }.collect {
        postValue(UiState.Success(it))
    }
}