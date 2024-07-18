package com.joohnq.sppublicbus.common.model.repository

import com.google.common.truth.Truth
import com.joohnq.sppublicbus.BuildConfig
import com.joohnq.sppublicbus.MockWebServerHelper
import com.joohnq.sppublicbus.common.Constants
import com.joohnq.sppublicbus.common.exception.NetworkAPIException
import com.joohnq.sppublicbus.model.entity.BusStop
import com.joohnq.sppublicbus.model.repository.StopsRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class StopsRepositoryTest {
    @get:Rule val rule = HiltAndroidRule(this)
    @Inject lateinit var repository: StopsRepository
    private lateinit var mockWebServer: MockWebServer
    private lateinit var mockWebServerHelper: MockWebServerHelper
    private suspend fun getFlowBusStops(): Flow<List<BusStop>> =
        repository.getBusPoints("search")

    private suspend fun getFlowStopsByRunners(): Flow<List<BusStop>> =
        repository.getStopsByRunner(1)

    @Before
    fun setUp() {
        rule.inject()
        mockWebServer = MockWebServer()
        mockWebServer.start(Constants.TEST_PORT)
        mockWebServerHelper = MockWebServerHelper(mockWebServer, "stops_success.json", null)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun verifyIfTheInterceptorsAreAddingTokenOnUrl_getFlowBusStops_shouldReturnTheToke() = runTest {
        mockWebServerHelper.enqueue200Response()

        getFlowBusStops().collect {
            val recordedRequest = mockWebServerHelper.takeRequest()
            Truth.assertThat(recordedRequest?.path).isNotNull()
            Truth.assertThat(recordedRequest?.path)
                .contains("token=${BuildConfig.API_SP_TRANS_KEY}")
        }
    }

    @Test
    fun verifyWithValidResponse_getFlowBusStops_shouldReturnListSize() = runTest {
        mockWebServerHelper.enqueue200Response()

        getFlowBusStops().collect { list ->
            Truth.assertThat(list.size).isEqualTo(4)
        }
    }

    @Test(expected = NetworkAPIException.EmptyListError::class)
    fun verifyWithVehicleEmptyList_getFlowBusStops_shouldReturnException() = runTest {
        mockWebServerHelper.enqueue200ResponseWithEmptyList()
        getFlowBusStops().collect {}
    }

    @Test(expected = NetworkAPIException.BodyNullError::class)
    fun verifyWithNullBody_getFlowBusStops_shouldReturnException() = runTest {
        mockWebServerHelper.enqueue200ResponseWithNullBody()
        getFlowBusStops().collect {}
    }

    @Test
    fun verifyWithValidResponse_getFlowStopsByRunners_shouldReturnListSize() = runTest {
        mockWebServerHelper.enqueue200Response()

        getFlowStopsByRunners().collect { list ->
            Truth.assertThat(list.size).isEqualTo(4)
        }
    }

    @Test(expected = NetworkAPIException.EmptyListError::class)
    fun verifyWithVehicleEmptyList_getFlowStopsByRunners_shouldReturnException() = runTest {
        mockWebServerHelper.enqueue200ResponseWithEmptyList()
        getFlowStopsByRunners().collect {}
    }

    @Test(expected = NetworkAPIException.BodyNullError::class)
    fun verifyWithNullBody_getFlowStopsByRunners_shouldReturnException() = runTest {
        mockWebServerHelper.enqueue200ResponseWithNullBody()
        getFlowStopsByRunners().collect {}
    }
}