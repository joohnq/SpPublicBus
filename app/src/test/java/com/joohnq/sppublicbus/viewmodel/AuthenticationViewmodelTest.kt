package com.joohnq.sppublicbus.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.joohnq.sppublicbus.common.exception.NetworkAPIException
import com.joohnq.sppublicbus.common.state.UiState
import com.joohnq.sppublicbus.model.repository.AuthenticationRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
class AuthenticationViewmodelTest {
    @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()
    @Mock private lateinit var repository: AuthenticationRepository
    private lateinit var viewmodel: AuthenticationViewmodel
    private val dispatcher: TestDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewStates: MutableList<UiState<Boolean>>

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewStates = mutableListOf()
        viewmodel = AuthenticationViewmodel(
            repository = repository,
            dispatcher = dispatcher
        )
        viewmodel.authentication.observeForever { viewStates.add(it) }
    }

    @Test
    fun `test authentication with true, should return a success`() = runTest {
        Mockito.`when`(
            repository.authentication()
        ).thenReturn(flowOf(true))

        Truth.assertThat(viewStates).isEmpty()

        viewmodel.authentication()

        Truth.assertThat(viewStates[0]).isEqualTo(UiState.Loading)
        Truth.assertThat(viewStates[1]).isEqualTo(UiState.Success(true))
    }

    @Test
    fun `test authentication with exception, the value should be UiState Error`() =
        runTest(dispatcher) {
            val exception = NetworkAPIException.AuthenticationError()
            Mockito.`when`(
                repository.authentication()
            ).thenReturn(
                flow { throw exception }
            )

            Truth.assertThat(viewStates).isEmpty()

            viewmodel.authentication()

            Truth.assertThat(viewStates[0]).isEqualTo(UiState.Loading)
            Truth.assertThat(viewStates[1]).isEqualTo(UiState.Error(exception.message))
        }
}