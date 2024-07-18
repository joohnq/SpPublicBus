package com.joohnq.sppublicbus.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.joohnq.sppublicbus.MainDispatcherRule
import com.joohnq.sppublicbus.common.exception.NetworkAPIException
import com.joohnq.sppublicbus.common.state.UiState
import com.joohnq.sppublicbus.model.entity.BusLinesRequestItem
import com.joohnq.sppublicbus.model.repository.BusLineRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
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
class BusLinesViewmodelTest {
    @get:Rule val mainDispatcherRule = MainDispatcherRule()
    @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()
    @Mock private lateinit var repository: BusLineRepository
    @Mock private lateinit var list: List<BusLinesRequestItem>
    private val dispatcher: TestDispatcher = UnconfinedTestDispatcher()
    private val search: String = "Teste"
    private lateinit var viewmodel: BusLinesViewmodel
    private lateinit var listFlow: Flow<List<BusLinesRequestItem>>
    private lateinit var viewStates: MutableList<UiState<List<BusLinesRequestItem>>>

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        listFlow = flowOf(list)
        viewStates = mutableListOf()
        viewmodel = BusLinesViewmodel(
            repository = repository,
            dispatcher = dispatcher
        )
        viewmodel.busLinesSearch.observeForever { viewStates.add(it) }
    }

    private suspend fun mockitoWhenSuccess() {
        Mockito.`when`(
            repository.getBusLines(search)
        ).thenReturn(listFlow)

        Truth.assertThat(viewStates).isEmpty()
    }

    private suspend fun Throwable.mockitoWhenError() {
        val exception = this
        Mockito.`when`(
            repository.getBusLines(search)
        ).thenReturn(
            flow { throw exception }
        )

        Truth.assertThat(viewStates).isEmpty()
    }

    private fun mockitoWhenSuccessTruth() {
        Truth.assertThat(viewStates[0]).isEqualTo(UiState.Loading)
        Truth.assertThat(viewStates[1]).isEqualTo(UiState.Success(list))
    }

    private fun Throwable.mockitoWhenErrorTruth() {
        Truth.assertThat(viewStates[0]).isEqualTo(UiState.Loading)
        Truth.assertThat(viewStates[1]).isEqualTo(UiState.Error(message.toString()))
    }

    @Test
    fun `test getBusLines with success list, should return UiState Success`() =
        runTest {
            mockitoWhenSuccess()
            viewmodel.getBusLines(search)
            mockitoWhenSuccessTruth()
        }

    @Test
    fun `test getBusLines with exception, should return UiState Error`() = runTest {
        val exception = NetworkAPIException.AuthenticationError()
        exception.mockitoWhenError()
        viewmodel.getBusLines(search)
        exception.mockitoWhenErrorTruth()
    }
}