package com.joohnq.sppublicbus.common.state

sealed class UiState<out T> {
    data object None : UiState<Nothing>()
    data object Loading : UiState<Nothing>()
    data class Success<out T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()

    fun <T> UiState<T>.getDataOrNull(): T? {
        return when (this) {
            is Success -> data
            else -> null
        }
    }

    fun <R> fold(
        onLoading: (() -> R)? = null,
        onSuccess: ((T) -> R)? = null,
        onError: ((String) -> R)? = null,
        onNone: (() -> R)? = null
    ): R {
        return when (this) {
            is Loading -> onLoading?.let { it() } as R
            is Success -> onSuccess?.let { it(data) } as R
            is Error -> onError?.let { it(message) } as R
            is None -> onNone?.let { it() } as R
        }
    }
}