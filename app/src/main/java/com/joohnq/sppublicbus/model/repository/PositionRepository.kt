package com.joohnq.sppublicbus.model.repository

import com.joohnq.sppublicbus.common.exception.NetworkAPIException
import com.joohnq.sppublicbus.model.entity.PositionBusByLinesRequest
import com.joohnq.sppublicbus.model.services.remote.SpTransService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PositionRepository @Inject constructor(
    private val service: SpTransService
) {
    suspend fun getPositionVehiclesByBusLines(id: Int): Flow<PositionBusByLinesRequest> = flow {
        try {
            val res = service.getPositionBusLines(id)
            if (res.isSuccessful) {
                val body = res.body() ?: throw NetworkAPIException.BodyNullError()
                if (body.vs.isEmpty()) throw NetworkAPIException.EmptyListError()

                emit(body)
            }
        } catch (e: Exception) {
            throw e
        }


    }
}