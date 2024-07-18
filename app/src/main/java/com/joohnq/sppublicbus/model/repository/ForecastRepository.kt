package com.joohnq.sppublicbus.model.repository

import com.joohnq.sppublicbus.common.exception.NetworkAPIException
import com.joohnq.sppublicbus.model.entity.ForecastStopRequest
import com.joohnq.sppublicbus.model.services.remote.SpTransService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ForecastRepository @Inject constructor(
    private val service: SpTransService
) {
    suspend fun getForecastByStop(id: Int): Flow<ForecastStopRequest> = flow {
        try {
            val res = service.getForecastByStop(id)

            if (res.isSuccessful) {
                val body = res.body() ?: throw NetworkAPIException.BodyNullError()
                if(body.p == null) throw NetworkAPIException.BodyNullError()
                if (body.p.l.isEmpty()) throw NetworkAPIException.EmptyListError()

                emit(body)
            }
        } catch (e: Exception) {
            throw e
        }
    }
}