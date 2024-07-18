package com.joohnq.sppublicbus.common.model.repository

import com.google.common.truth.Truth
import com.joohnq.sppublicbus.BuildConfig
import com.joohnq.sppublicbus.MockWebServerHelper
import com.joohnq.sppublicbus.common.Constants
import com.joohnq.sppublicbus.common.exception.NetworkAPIException
import com.joohnq.sppublicbus.model.repository.AuthenticationRepository
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
class AuthenticationRepositoryTest {
    @get:Rule val rule = HiltAndroidRule(this)
    @Inject lateinit var repository: AuthenticationRepository
    private lateinit var mockWebServer: MockWebServer
    private lateinit var mockWebServerHelper: MockWebServerHelper
    private suspend fun getFlow(): Flow<Boolean> =
        repository.authentication()

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
    fun verifyIfTheAuthenticatorIsCalled_shouldReturnTheToken() = runTest {
        mockWebServerHelper.enqueue200ResponseWithBody("true")

        getFlow().collect {
            val recordedRequest = mockWebServerHelper.takeRequest()
            Truth.assertThat(recordedRequest?.path).contains(Constants.API_PATH_AUTHENTICATION)
            Truth.assertThat(recordedRequest?.requestUrl?.queryParameter("token"))
                .isEqualTo(BuildConfig.API_SP_TRANS_KEY)
        }
    }

    @Test
    fun verifyWithValidResponse_shouldReturnTrue() = runTest {
        mockWebServerHelper.enqueue200ResponseWithBody("true")

        getFlow().collect {
            Truth.assertThat(it).isEqualTo(true)
        }
    }

    @Test(expected = NetworkAPIException.AuthenticationError::class)
    fun verifyWithFailedAuthentication_shouldReturnException() = runTest {
        mockWebServerHelper.enqueue200ResponseWithBody("false")
        getFlow().collect {}
    }

    @Test(expected = NetworkAPIException.BodyNullError::class)
    fun verifyWithNullBody_shouldReturnException() = runTest {
        mockWebServerHelper.enqueue200ResponseWithNullBody()
        getFlow().collect {}
    }
}