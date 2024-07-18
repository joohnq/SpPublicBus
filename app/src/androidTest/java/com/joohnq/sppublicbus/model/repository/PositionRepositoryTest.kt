package com.joohnq.sppublicbus.common.model.repository

import com.google.common.truth.Truth
import com.joohnq.sppublicbus.BuildConfig
import com.joohnq.sppublicbus.MockWebServerHelper
import com.joohnq.sppublicbus.common.Constants
import com.joohnq.sppublicbus.common.exception.NetworkAPIException
import com.joohnq.sppublicbus.model.entity.PositionBusByLinesRequest
import com.joohnq.sppublicbus.model.repository.PositionRepository
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
class PositionRepositoryTest {
    @get:Rule val rule = HiltAndroidRule(this)
    @Inject lateinit var repository: PositionRepository
    private lateinit var mockWebServer: MockWebServer
    private lateinit var mockWebServerHelper: MockWebServerHelper
    private suspend fun getFlow(): Flow<PositionBusByLinesRequest> =
        repository.getPositionVehiclesByBusLines(1)

    @Before
    fun setUp() {
        rule.inject()
        mockWebServer = MockWebServer()
        mockWebServer.start(Constants.TEST_PORT)
        mockWebServerHelper = MockWebServerHelper(mockWebServer, "position_success.json", "position_with_vehicles_empty.json")
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
    fun verifyWithValidResponse_shouldReturnListSize() = runTest {
        mockWebServerHelper.enqueue200Response()

        getFlow().collect { list ->
            Truth.assertThat(list.vs.size).isEqualTo(2)
        }
    }

    @Test(expected = NetworkAPIException.EmptyListError::class)
    fun verifyWithEmptyList_shouldReturnException() = runTest {
        mockWebServerHelper.enqueue200ResponseWithEmptyListFile()
        getFlow().collect {}
    }

    @Test(expected = NetworkAPIException.BodyNullError::class)
    fun verifyWithNullBody_shouldReturnException() = runTest {
        mockWebServerHelper.enqueue200ResponseWithNullBody()
        getFlow().collect {}
    }
}