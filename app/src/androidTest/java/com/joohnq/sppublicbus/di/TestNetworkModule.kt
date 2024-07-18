package com.joohnq.sppublicbus.di

import com.joohnq.sppublicbus.BuildConfig
import com.joohnq.sppublicbus.di.annotations.InterceptorHTTPLogging
import com.joohnq.sppublicbus.di.annotations.InterceptorToken
import com.joohnq.sppublicbus.di.annotations.OkHttpClientAuthService
import com.joohnq.sppublicbus.di.annotations.OkHttpClientSpTransService
import com.joohnq.sppublicbus.model.services.auth.AuthAuthenticator
import com.joohnq.sppublicbus.model.services.auth.AuthService
import com.joohnq.sppublicbus.model.services.remote.SpTransService
import com.joohnq.sppublicbus.model.services.remote.TokenInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [NetworkModule::class])
object TestNetworkModule {
    @InterceptorHTTPLogging
    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): Interceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    @InterceptorToken
    @Provides
    @Singleton
    fun provideTokenInterceptor(): Interceptor =
        TokenInterceptor(BuildConfig.API_SP_TRANS_KEY)

    @Singleton
    @Provides
    fun provideAuthAuthenticator(
        authService: AuthService,
    ): Authenticator = AuthAuthenticator(BuildConfig.API_SP_TRANS_KEY, authService)

    @Singleton
    @Provides
    fun provideRetrofitAuthService(
        @OkHttpClientAuthService okHttpClient: OkHttpClient,
    ): AuthService = Retrofit.Builder()
        .baseUrl("http://localhost:8080/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(AuthService::class.java)

    @Provides
    @Singleton
    fun provideRetrofitSpTransService(
        @OkHttpClientSpTransService okHttpClient: OkHttpClient,
    ): SpTransService = Retrofit.Builder()
        .baseUrl("http://localhost:8080/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(SpTransService::class.java)

    @OkHttpClientSpTransService
    @Provides
    @Singleton
    fun provideOkHttpClientSpTransService(
        authAuthenticator: Authenticator,
        @InterceptorToken tokenInterceptor: Interceptor,
        @InterceptorHTTPLogging httpLoggingInterceptor: Interceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .authenticator(authAuthenticator)
        .addInterceptor(httpLoggingInterceptor)
        .addInterceptor(tokenInterceptor)
        .build()

    @OkHttpClientAuthService
    @Provides
    @Singleton
    fun provideOkHttpClientAuthService(
        @InterceptorToken tokenInterceptor: Interceptor,
        @InterceptorHTTPLogging httpLoggingInterceptor: Interceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .addInterceptor(tokenInterceptor)
        .build()
}

