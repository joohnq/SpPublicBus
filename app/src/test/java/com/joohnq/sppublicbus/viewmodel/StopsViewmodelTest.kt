package com.joohnq.sppublicbus.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.joohnq.sppublicbus.MainDispatcherRule
import com.joohnq.sppublicbus.common.exception.NetworkAPIException
import com.joohnq.sppublicbus.common.state.UiState
import com.joohnq.sppublicbus.model.entity.BusStop
import com.joohnq.sppublicbus.model.repository.StopsRepository
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
class StopsViewmodelTest {
    @get:Rule val mainDispatcherRule = MainDispatcherRule()
    @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()
    @Mock private lateinit var repository: StopsRepository
    @Mock private lateinit var item: List<BusStop>
    private lateinit var itemFlow: Flow<List<BusStop>>
    private val dispatcher: TestDispatcher = UnconfinedTestDispatcher()
    private val search: String = ""
    private val id: Int = 1
    private lateinit var viewmodel: StopsViewmodel
    private lateinit var viewStates: MutableList<UiState<List<BusStop>>>

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        itemFlow = flowOf(item)
        viewStates = mutableListOf()
        viewmodel = StopsViewmodel(
            repository = repository,
            dispatcher = dispatcher
        )
        viewmodel.busPointsSearch.observeForever { viewStates.add(it) }
    }

    private suspend fun mockitoWhenSuccess() {
        Mockito.`when`(
            repository.getBusPoints(search)
        ).thenReturn(itemFlow)

        Truth.assertThat(viewStates).isEmpty()
    }

    private suspend fun Throwable.mockitoWhenError() {
        val exception = this
        Mockito.`when`(
            repository.getBusPoints(search)
        ).thenReturn(
            flow { throw exception }
        )

        Truth.assertThat(viewStates).isEmpty()
    }

    private fun mockitoWhenSuccessTruth() {
        Truth.assertThat(viewStates[0]).isEqualTo(UiState.Loading)
        Truth.assertThat(viewStates[1]).isEqualTo(UiState.Success(item))
    }

    private fun Throwable.mockitoWhenErrorTruth() {
        Truth.assertThat(viewStates[0]).isEqualTo(UiState.Loading)
        Truth.assertThat(viewStates[1]).isEqualTo(UiState.Error(message.toString()))
    }

    @Test
    fun `test getBusPoints with success item, should return UiState Success`() =
        runTest {
            mockitoWhenSuccess()
            viewmodel.getBusPoints(search)
            mockitoWhenSuccessTruth()
        }

    @Test
    fun `test getBusPoints with exception, should return UiState Error`() = runTest {
        val exception = NetworkAPIException.BodyNullError()
        exception.mockitoWhenError()
        viewmodel.getBusPoints(search)
        exception.mockitoWhenErrorTruth()
    }

    @Test
    fun `test getStopsByRunner with success item, should return UiState Success`() =
        runTest {
            mockitoWhenSuccess()
            viewmodel.getStopsByRunner(id)
            mockitoWhenSuccessTruth()
        }

    @Test
    fun `test getStopsByRunner with exception, should return UiState Error`() =
        runTest {
            val exception = NetworkAPIException.BodyNullError()
            exception.mockitoWhenError()
            viewmodel.getStopsByRunner(id)
            exception.mockitoWhenErrorTruth()
        }
}