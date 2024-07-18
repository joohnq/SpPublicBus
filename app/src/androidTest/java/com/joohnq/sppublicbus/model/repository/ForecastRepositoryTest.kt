package com.joohnq.sppublicbus.common.model.repository

import com.google.common.truth.Truth
import com.joohnq.sppublicbus.BuildConfig
import com.joohnq.sppublicbus.MockWebServerHelper
import com.joohnq.sppublicbus.common.Constants
import com.joohnq.sppublicbus.common.exception.NetworkAPIException
import com.joohnq.sppublicbus.model.entity.ForecastStopRequest
import com.joohnq.sppublicbus.model.repository.ForecastRepository
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
class ForecastRepositoryTest {
    @get:Rule val rule = HiltAndroidRule(this)
    @Inject lateinit var repository: ForecastRepository
    private lateinit var mockWebServer: MockWebServer
    private lateinit var mockWebServerHelper: MockWebServerHelper
    private suspend fun getFlow(): Flow<ForecastStopRequest> =
        repository.getForecastByStop(1)

    @Before
    fun setUp() {
        rule.inject()
        mockWebServer = MockWebServer()
        mockWebServer.start(Constants.TEST_PORT)
        mockWebServerHelper = MockWebServerHelper(
            mockWebServer,
            "forecast_success.json",
            "forecast_with_lines_empty.json"
        )
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun verifyIfTheInterceptorsAreAddingTokenOnUrl_shouldReturnTheToke() = runTest {
        mockWebServerHelper.enqueue200Response()

        getFlow().collect {
            val recordedRequest = mockWebServerHelper.takeRequest()
            Truth.assertThat(recordedRequest?.path).isNotNull()
            Truth.assertThat(recordedRequest?.path)
                .contains("token=${BuildConfig.API_SP_TRANS_KEY}")
        }
    }

    @Test
    fun verifyWithValidResponse_shouldReturnTheHourAndVehicleListSize() = runTest {
        mockWebServerHelper.enqueue200Response()

        getFlow().collect { item ->
            Truth.assertThat(item).isNotNull()
            Truth.assertThat(item.hr).isNotEqualTo("")
            Truth.assertThat(item.p?.l?.size).isEqualTo(2)
        }
    }

    @Test(expected = NetworkAPIException.EmptyListError::class)
    fun verifyWithVehicleEmptyList_shouldReturnException() = runTest {
        mockWebServerHelper.enqueue200ResponseWithEmptyListFile()
        getFlow().collect {}
    }

    @Test(expected = NetworkAPIException.BodyNullError::class)
    fun verifyWithNullBody_shouldReturnException() = runTest {
        mockWebServerHelper.enqueue200ResponseWithNullBody()
        getFlow().collect {}
    }
}