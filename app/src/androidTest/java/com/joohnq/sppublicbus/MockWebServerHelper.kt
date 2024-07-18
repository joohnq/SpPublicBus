package com.joohnq.sppublicbus

import com.joohnq.sppublicbus.common.FileReader
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import java.util.concurrent.TimeUnit

class MockWebServerHelper(
    private val mockWebServer: MockWebServer,
    private val bodyFile: String?,
    private val bodyWithListEmptyFile: String?
) {
    private val mockResponse401 = MockResponse().setResponseCode(401).setBody("Unauthorized")
    private val mockResponse400 = MockResponse().setResponseCode(400).setBody("Not Found")
    private val mockResponse200WithNullBody =
        MockResponse().setResponseCode(200).setBody("null")
    private val mockResponse200WithEmptyList =
        MockResponse().setResponseCode(200).setBody("[]")

    fun takeRequest(): RecordedRequest? =
        mockWebServer.takeRequest(5, TimeUnit.SECONDS)

    fun takeRequest(times: Int): Unit = repeat(times) {
        mockWebServer.takeRequest(5, TimeUnit.SECONDS)
    }

    fun enqueue200Response(times: Int = 1) =
        repeat(times) {
            mockWebServer.enqueue(
                MockResponse().setResponseCode(200).setBody(
                    FileReader.readStringFromFile(
                        bodyFile ?: throw Exception("bodyWithListEmptyFile is null")
                    )
                )
            )
        }

    fun enqueue200ResponseWithFileBody(file: String, times: Int = 1) =
        repeat(times) {
            mockWebServer.enqueue(
                MockResponse().setResponseCode(200).setBody(
                    FileReader.readStringFromFile(
                        file ?: throw Exception("bodyWithListEmptyFile is null")
                    )
                )
            )
        }

    fun enqueue400Response(times: Int = 1) = repeat(times) {
        mockWebServer.enqueue(mockResponse400)
    }

    fun enqueue401Response(times: Int = 1) = repeat(times) {
        mockWebServer.enqueue(mockResponse401)
    }

    fun enqueue200ResponseWithBody(body: String, times: Int = 1) = repeat(times) {
        mockWebServer.enqueue(
            MockResponse().setResponseCode(200).setBody(body)
        )
    }

    fun enqueue200ResponseWithEmptyListFile() =
        mockWebServer.enqueue(
            MockResponse().setResponseCode(200).setBody(
                FileReader.readStringFromFile(
                    bodyWithListEmptyFile ?: throw Exception("bodyWithListEmptyFile is null")
                )
            )
        )

    fun enqueue200ResponseWithEmptyList() =
        mockWebServer.enqueue(mockResponse200WithEmptyList)

    fun enqueue200ResponseWithNullBody() =
        mockWebServer.enqueue(mockResponse200WithNullBody)
}