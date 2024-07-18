package com.joohnq.sppublicbus.model.services.remote

import com.joohnq.sppublicbus.common.Constants.API_BUS_LINES
import com.joohnq.sppublicbus.common.Constants.API_BUS_LINE_POSITION
import com.joohnq.sppublicbus.common.Constants.API_FIND_STOPS_BY_RUNNER
import com.joohnq.sppublicbus.common.Constants.API_FORECAST_BY_STOP
import com.joohnq.sppublicbus.common.Constants.API_STOPS
import com.joohnq.sppublicbus.common.Constants.API_STREET_RUNNER
import com.joohnq.sppublicbus.model.entity.BusLinesRequestItem
import com.joohnq.sppublicbus.model.entity.BusStop
import com.joohnq.sppublicbus.model.entity.ForecastStopRequest
import com.joohnq.sppublicbus.model.entity.PositionBusByLinesRequest
import com.joohnq.sppublicbus.model.entity.StreetRunner
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SpTransService {
    @GET(API_BUS_LINES)
    suspend fun getBusLines(
        @Query("termosBusca") search: String
    ): Response<List<BusLinesRequestItem>>

    @GET(API_STOPS)
    suspend fun getBusPoints(
        @Query("termosBusca") search: String
    ): Response<List<BusStop>>

    @GET(API_STREET_RUNNER)
    suspend fun getRunners(): Response<List<StreetRunner>>

    @GET(API_FIND_STOPS_BY_RUNNER)
    suspend fun getStopsByRunner(
        @Query("codigoCorredor") id: String
    ): Response<List<BusStop>>

    @GET(API_BUS_LINE_POSITION)
    suspend fun getPositionBusLines(
        @Query("codigoLinha") id: Int
    ): Response<PositionBusByLinesRequest>

    @GET(API_FORECAST_BY_STOP)
    suspend fun getForecastByStop(
        @Query("codigoParada") id: Int
    ): Response<ForecastStopRequest>
}