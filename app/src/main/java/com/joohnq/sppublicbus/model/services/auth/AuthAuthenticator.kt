package com.joohnq.sppublicbus.model.services.auth

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

class AuthAuthenticator @Inject constructor(
    private val token: String,
    private val service: AuthService
) : Authenticator {
    private var tokenRefreshInProgress: AtomicBoolean = AtomicBoolean(false)
    private var request: Request? = null

    override fun authenticate(route: Route?, response: Response): Request? {
        return runBlocking {
            println("Executou")
            request = null
            val req = response.request

            if (!tokenRefreshInProgress.get()) {
                tokenRefreshInProgress.set(true)
                authentication()
                request = buildRequest(req)
                tokenRefreshInProgress.set(false)
            } else {
                waitForRefresh(req)
            }

            if (responseCount(response) >= 3) {
                null
            } else request
        }
    }

    private suspend fun waitForRefresh(req: Request) {
        while (tokenRefreshInProgress.get()) {
            delay(100)
        }
        request = buildRequest(req)
    }


    private fun buildRequest(req: Request): Request {
        return req.newBuilder()
            .url(req.url.newBuilder().setQueryParameter("token", token).build())
            .build()
    }

    private fun responseCount(response: Response?): Int {
        var result = 1
        while (response?.priorResponse != null && result <= 3) {
            result++
        }
        return result
    }

    private suspend fun authentication(): Boolean {
        val authRes = service.authentication()
        return authRes.isSuccessful
    }
}

