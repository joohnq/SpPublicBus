package com.joohnq.sppublicbus.common.model.services.auth

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.joohnq.sppublicbus.MockWebServerHelper
import com.joohnq.sppublicbus.common.Constants
import com.joohnq.sppublicbus.model.entity.BusLinesRequestItem
import com.joohnq.sppublicbus.model.entity.BusStop
import com.joohnq.sppublicbus.model.entity.ForecastStopRequest
import com.joohnq.sppublicbus.model.entity.PositionBusByLinesRequest
import com.joohnq.sppublicbus.model.entity.StreetRunner
import com.joohnq.sppublicbus.model.repository.BusLineRepository
import com.joohnq.sppublicbus.model.repository.ForecastRepository
import com.joohnq.sppublicbus.model.repository.PositionRepository
import com.joohnq.sppublicbus.model.repository.StopsRepository
import com.joohnq.sppublicbus.model.repository.StreetRunnersRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class AuthAuthenticatorTest {
    @get:Rule val instantExecutorRule = InstantTaskExecutorRule()
    @get:Rule val rule = HiltAndroidRule(this)
    @Inject lateinit var positionRepository: PositionRepository
    @Inject lateinit var busLineRepository: BusLineRepository
    @Inject lateinit var stopsRepository: StopsRepository
    @Inject lateinit var forecastRepository: ForecastRepository
    @Inject lateinit var streetRunnersRepository: StreetRunnersRepository
    private lateinit var mockWebServer: MockWebServer
    private lateinit var mockWebServerHelper: MockWebServerHelper
    private suspend fun getPositionVehiclesByBusLines(): Flow<PositionBusByLinesRequest> =
        positionRepository.getPositionVehiclesByBusLines(1)

    private suspend fun getBusLines(): Flow<List<BusLinesRequestItem>> =
        busLineRepository.getBusLines("")

    private suspend fun getBusPoints(): Flow<List<BusStop>> =
        stopsRepository.getBusPoints("")

    private suspend fun getStopsByRunner(): Flow<List<BusStop>> =
        stopsRepository.getStopsByRunner(1)

    private suspend fun getForecastByStop(): Flow<ForecastStopRequest> =
        forecastRepository.getForecastByStop(1)

    private suspend fun getStreetRunners(): Flow<List<StreetRunner>> =
        streetRunnersRepository.getStreetRunners()

    @Before
    fun setUp() {
        rule.inject()
        mockWebServer = MockWebServer()
        mockWebServer.start(Constants.TEST_PORT)
        mockWebServerHelper = MockWebServerHelper(mockWebServer, null, null)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun verifyIfTheAuthenticatorAfterTwo401_getPositionVehiclesByBusLines_shouldReturnNotNull() =
        runTest {
            mockWebServerHelper.enqueue401Response(times = 2)
            mockWebServerHelper.enqueue200ResponseWithFileBody("position_success.json")

            getPositionVehiclesByBusLines().collect {
                mockWebServerHelper.takeRequest(times = 2)

                val recordedRequestAfterLimit = mockWebServerHelper.takeRequest()
                Truth.assertThat(recordedRequestAfterLimit).isNotNull()
            }
        }

    @Test
    fun verifyIfTheAuthenticatorStopBeenCalledAfterThree401_getPositionVehiclesByBusLines_shouldReturnNull() =
        runBlocking {
            mockWebServerHelper.enqueue401Response(times = 4)
            mockWebServerHelper.enqueue200ResponseWithFileBody("position_success.json")

            getPositionVehiclesByBusLines().collect {
                mockWebServerHelper.takeRequest(times = 4)

                val recordedRequestAfterLimit = mockWebServerHelper.takeRequest()
                Truth.assertThat(recordedRequestAfterLimit).isNull()
            }
        }

    @Test
    fun verifyIfTheAuthenticatorAfterTwo401_getBusLines_shouldReturnNotNull() = runTest {
        mockWebServerHelper.enqueue401Response(times = 2)
        mockWebServerHelper.enqueue200ResponseWithFileBody("bus_line_success.json")

        getBusLines().collect {
            mockWebServerHelper.takeRequest(times = 2)

            val recordedRequestAfterLimit = mockWebServerHelper.takeRequest()
            Truth.assertThat(recordedRequestAfterLimit).isNotNull()
        }
    }

    @Test
    fun verifyIfTheAuthenticatorStopBeenCalledAfterThree401_getBusLines_shouldReturnNull() =
        runBlocking {
            mockWebServerHelper.enqueue401Response(times = 4)
            mockWebServerHelper.enqueue200ResponseWithFileBody("bus_line_success.json")

            getBusLines().collect {
                mockWebServerHelper.takeRequest(times = 4)

                val recordedRequestAfterLimit = mockWebServerHelper.takeRequest()
                Truth.assertThat(recordedRequestAfterLimit).isNull()
            }
        }

    @Test
    fun verifyIfTheAuthenticatorAfterTwo401_getBusPoints_shouldReturnNotNull() = runTest {
        mockWebServerHelper.enqueue401Response(times = 2)
        mockWebServerHelper.enqueue200ResponseWithFileBody("stops_success.json")

        getBusPoints().collect {
            mockWebServerHelper.takeRequest(times = 2)

            val recordedRequestAfterLimit = mockWebServerHelper.takeRequest()
            Truth.assertThat(recordedRequestAfterLimit).isNotNull()
        }
    }

    @Test
    fun verifyIfTheAuthenticatorStopBeenCalledAfterThree401_getBusPoints_shouldReturnNull() =
        runBlocking {
            mockWebServerHelper.enqueue401Response(times = 4)
            mockWebServerHelper.enqueue200ResponseWithFileBody("stops_success.json")

            getBusPoints().collect {
                mockWebServerHelper.takeRequest(times = 4)

                val recordedRequestAfterLimit = mockWebServerHelper.takeRequest()
                Truth.assertThat(recordedRequestAfterLimit).isNull()
            }
        }

    @Test
    fun verifyIfTheAuthenticatorAfterTwo401_getStopsByRunner_shouldReturnNotNull() = runTest {
        mockWebServerHelper.enqueue401Response(times = 2)
        mockWebServerHelper.enqueue200ResponseWithFileBody("stops_success.json")

        getStopsByRunner().collect {
            mockWebServerHelper.takeRequest(times = 2)

            val recordedRequestAfterLimit = mockWebServerHelper.takeRequest()
            Truth.assertThat(recordedRequestAfterLimit).isNotNull()
        }
    }

    @Test
    fun verifyIfTheAuthenticatorStopBeenCalledAfterThree401_getStopsByRunner_shouldReturnNull() =
        runBlocking {
            mockWebServerHelper.enqueue401Response(times = 4)
            mockWebServerHelper.enqueue200ResponseWithFileBody("stops_success.json")

            getStopsByRunner().collect {
                mockWebServerHelper.takeRequest(times = 4)

                val recordedRequestAfterLimit = mockWebServerHelper.takeRequest()
                Truth.assertThat(recordedRequestAfterLimit).isNull()
            }
        }

    @Test
    fun verifyIfTheAuthenticatorAfterTwo401_getForecastByStop_shouldReturnNotNull() = runTest {
        mockWebServerHelper.enqueue401Response(times = 2)
        mockWebServerHelper.enqueue200ResponseWithFileBody("forecast_success.json")

        getForecastByStop().collect {
            mockWebServerHelper.takeRequest(times = 2)

            val recordedRequestAfterLimit = mockWebServerHelper.takeRequest()
            Truth.assertThat(recordedRequestAfterLimit).isNotNull()
        }
    }

    @Test
    fun verifyIfTheAuthenticatorStopBeenCalledAfterThree401_getForecastByStop_shouldReturnNull() =
        runBlocking {
            mockWebServerHelper.enqueue401Response(times = 4)
            mockWebServerHelper.enqueue200ResponseWithFileBody("forecast_success.json")

            getForecastByStop().collect {
                mockWebServerHelper.takeRequest(times = 4)

                val recordedRequestAfterLimit = mockWebServerHelper.takeRequest()
                Truth.assertThat(recordedRequestAfterLimit).isNull()
            }
        }

    @Test
    fun verifyIfTheAuthenticatorAfterTwo401_getStreetRunners_shouldReturnNotNull() = runTest {
        mockWebServerHelper.enqueue401Response(times = 2)
        mockWebServerHelper.enqueue200ResponseWithFileBody("street_runners_success.json")

        getStreetRunners().collect {
            mockWebServerHelper.takeRequest(times = 2)

            val recordedRequestAfterLimit = mockWebServerHelper.takeRequest()
            Truth.assertThat(recordedRequestAfterLimit).isNotNull()
        }
    }

    @Test
    fun verifyIfTheAuthenticatorStopBeenCalledAfterThree401_getStreetRunners_shouldReturnNull() =
        runBlocking {
            mockWebServerHelper.enqueue401Response(times = 4)
            mockWebServerHelper.enqueue200ResponseWithFileBody("street_runners_success.json")

            getStreetRunners().collect {
                mockWebServerHelper.takeRequest(times = 4)

                val recordedRequestAfterLimit = mockWebServerHelper.takeRequest()
                Truth.assertThat(recordedRequestAfterLimit).isNull()
            }
        }
}