package com.joohnq.sppublicbus.di

import com.joohnq.sppublicbus.model.repository.AuthenticationRepository
import com.joohnq.sppublicbus.model.repository.BusLineRepository
import com.joohnq.sppublicbus.model.repository.ForecastRepository
import com.joohnq.sppublicbus.model.repository.PositionRepository
import com.joohnq.sppublicbus.model.repository.StopsRepository
import com.joohnq.sppublicbus.model.repository.StreetRunnersRepository
import com.joohnq.sppublicbus.model.services.auth.AuthService
import com.joohnq.sppublicbus.model.services.remote.SpTransService
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [RepositoryModule::class])
object TestRepositoryModule {
    @Provides
    @Singleton
    fun provideAuthenticationRepository(
        authService: AuthService
    ): AuthenticationRepository = AuthenticationRepository(
        service = authService
    )

    @Provides
    @Singleton
    fun providePositionRepository(
        spTransService: SpTransService
    ): PositionRepository = PositionRepository(
        service = spTransService
    )

    @Provides
    @Singleton
    fun provideBusLineRepository(
        spTransService: SpTransService
    ): BusLineRepository = BusLineRepository(
        service = spTransService
    )

    @Provides
    @Singleton
    fun provideBusPointsRepository(
        spTransService: SpTransService
    ): StopsRepository = StopsRepository(
        service = spTransService
    )

    @Provides
    @Singleton
    fun provideStreetRunnersRepository(
        spTransService: SpTransService
    ): StreetRunnersRepository = StreetRunnersRepository(
        service = spTransService
    )

    @Provides
    @Singleton
    fun provideForecastRepository(
        spTransService: SpTransService
    ): ForecastRepository = ForecastRepository(
        service = spTransService
    )
}