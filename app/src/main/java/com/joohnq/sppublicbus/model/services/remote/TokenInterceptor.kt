package com.joohnq.sppublicbus.model.services.remote

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

class TokenInterceptor @Inject constructor(private val token: String) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Chain): Response {
        val originalRequest: Request = chain.request()
        val originalUrl: HttpUrl = originalRequest.url

        val url = originalUrl.newBuilder()
            .addQueryParameter("token", token)
            .build()

        val request: Request = originalRequest.newBuilder()
            .url(url)
            .build()

        return chain.proceed(request)
    }
}