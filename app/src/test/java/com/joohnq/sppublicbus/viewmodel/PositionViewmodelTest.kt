package com.joohnq.sppublicbus.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.joohnq.sppublicbus.MainDispatcherRule
import com.joohnq.sppublicbus.common.exception.NetworkAPIException
import com.joohnq.sppublicbus.common.state.UiState
import com.joohnq.sppublicbus.model.entity.PositionBusByLinesRequest
import com.joohnq.sppublicbus.model.repository.PositionRepository
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
class PositionViewmodelTest {
    @get:Rule val mainDispatcherRule = MainDispatcherRule()
    @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()
    @Mock private lateinit var repository: PositionRepository
    @Mock private lateinit var item: PositionBusByLinesRequest
    private lateinit var itemFlow: Flow<PositionBusByLinesRequest>
    private val dispatcher: TestDispatcher = UnconfinedTestDispatcher()
    private val id: Int = 1
    private lateinit var viewmodel: PositionViewmodel
    private lateinit var viewStates: MutableList<UiState<PositionBusByLinesRequest>>

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        itemFlow = flowOf(item)
        viewStates = mutableListOf()
        viewmodel = PositionViewmodel(
            repository = repository,
            dispatcher = dispatcher
        )
        viewmodel.positionVehicles.observeForever { viewStates.add(it) }
    }

    private suspend fun mockitoWhenSuccess() {
        Mockito.`when`(
            repository.getPositionVehiclesByBusLines(id)
        ).thenReturn(itemFlow)

        Truth.assertThat(viewStates).isEmpty()
    }

    private suspend fun Throwable.mockitoWhenError() {
        val exception = this
        Mockito.`when`(
            repository.getPositionVehiclesByBusLines(id)
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
    fun `test getPositionVehiclesByBusLines with success item, should return UiState Success`() =
        runTest {
            mockitoWhenSuccess()
            viewmodel.getPositionVehiclesByBusLines(id)
            mockitoWhenSuccessTruth()
        }

    @Test
    fun `test getPositionVehiclesByBusLines with exception, should return UiState Error`() =
        runTest {
            val exception = NetworkAPIException.BodyNullError()
            exception.mockitoWhenError()
            viewmodel.getPositionVehiclesByBusLines(id)
            exception.mockitoWhenErrorTruth()
        }
}