package com.joohnq.sppublicbus.di

import com.joohnq.sppublicbus.model.repository.AuthenticationRepository
import com.joohnq.sppublicbus.model.repository.BusLineRepository
import com.joohnq.sppublicbus.model.repository.ForecastRepository
import com.joohnq.sppublicbus.model.repository.StopsRepository
import com.joohnq.sppublicbus.model.repository.PositionRepository
import com.joohnq.sppublicbus.model.repository.StreetRunnersRepository
import com.joohnq.sppublicbus.viewmodel.AuthenticationViewmodel
import com.joohnq.sppublicbus.viewmodel.BusLinesViewmodel
import com.joohnq.sppublicbus.viewmodel.ForecastViewmodel
import com.joohnq.sppublicbus.viewmodel.StopsViewmodel
import com.joohnq.sppublicbus.viewmodel.PositionViewmodel
import com.joohnq.sppublicbus.viewmodel.StreetRunnersViewmodel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ViewModelModule {
    @Provides
    @Singleton
    fun provideAuthenticationViewmodel(
        authenticationRepository: AuthenticationRepository,
        ioDispatcher: CoroutineDispatcher
    ): AuthenticationViewmodel = AuthenticationViewmodel(
        repository = authenticationRepository,
        dispatcher = ioDispatcher
    )

    @Provides
    @Singleton
    fun providePositionViewmodel(
        positionRepository: PositionRepository,
        ioDispatcher: CoroutineDispatcher
    ): PositionViewmodel = PositionViewmodel(
        repository = positionRepository,
        dispatcher = ioDispatcher
    )

    @Provides
    @Singleton
    fun provideBusLinesViewmodel(
        busLinesRepository: BusLineRepository,
        ioDispatcher: CoroutineDispatcher
    ): BusLinesViewmodel = BusLinesViewmodel(
        repository = busLinesRepository,
        dispatcher = ioDispatcher
    )

    @Provides
    @Singleton
    fun providePointsViewmodel(
        stopsRepository: StopsRepository,
        ioDispatcher: CoroutineDispatcher
    ): StopsViewmodel = StopsViewmodel(
        repository = stopsRepository,
        dispatcher = ioDispatcher
    )

    @Provides
    @Singleton
    fun provideStreetRunnersViewmodel(
        streetRunnersRepository: StreetRunnersRepository,
        ioDispatcher: CoroutineDispatcher
    ): StreetRunnersViewmodel = StreetRunnersViewmodel(
        repository = streetRunnersRepository,
        dispatcher = ioDispatcher
    )

    @Provides
    @Singleton
    fun provideForecastViewmodel(
        forecastRepository: ForecastRepository,
        ioDispatcher: CoroutineDispatcher
    ): ForecastViewmodel = ForecastViewmodel(
        repository = forecastRepository,
        dispatcher = ioDispatcher
    )
}