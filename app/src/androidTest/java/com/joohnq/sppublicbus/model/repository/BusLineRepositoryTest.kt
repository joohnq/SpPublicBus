package com.joohnq.sppublicbus.model.repository

import com.google.common.truth.Truth
import com.joohnq.sppublicbus.BuildConfig
import com.joohnq.sppublicbus.MockWebServerHelper
import com.joohnq.sppublicbus.common.Constants
import com.joohnq.sppublicbus.common.exception.NetworkAPIException
import com.joohnq.sppublicbus.model.entity.BusLinesRequestItem
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
class BusLineRepositoryTest {
    @get:Rule val rule = HiltAndroidRule(this)
    @Inject lateinit var repository: BusLineRepository
    private lateinit var mockWebServer: MockWebServer
    private lateinit var mockWebServerHelper: MockWebServerHelper
    private suspend fun getFlow(): Flow<List<BusLinesRequestItem>> =
        repository.getBusLines("search")

    @Before
    fun setUp() {
        rule.inject()
        mockWebServer = MockWebServer()
        mockWebServer.start(Constants.TEST_PORT)
        mockWebServerHelper = MockWebServerHelper(mockWebServer, "bus_line_success.json", null)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun verifyIfTheInterceptorsAreAddingTokenOnUrl_shouldReturnTheToken() = runTest {
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
            Truth.assertThat(list.size).isEqualTo(5)
        }
    }

    @Test(expected = NetworkAPIException.EmptyListError::class)
    fun verifyWithEmptyList_shouldReturnException() = runTest {
        mockWebServerHelper.enqueue200ResponseWithEmptyList()
        getFlow().collect {}
    }

    @Test(expected = NetworkAPIException.BodyNullError::class)
    fun verifyWithNullBody_shouldReturnException() = runTest {
        mockWebServerHelper.enqueue200ResponseWithNullBody()
        getFlow().collect{}
    }
}