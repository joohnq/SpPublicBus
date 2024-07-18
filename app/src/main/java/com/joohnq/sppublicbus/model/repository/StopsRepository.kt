package com.joohnq.sppublicbus.model.repository

import com.joohnq.sppublicbus.common.exception.NetworkAPIException
import com.joohnq.sppublicbus.model.entity.BusStop
import com.joohnq.sppublicbus.model.services.remote.SpTransService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class StopsRepository @Inject constructor(
    private val service: SpTransService
) {
    suspend fun getBusPoints(search: String): Flow<List<BusStop>> = flow {
        try {
            val res = service.getBusPoints(search)
            if (res.isSuccessful) {
                val body = res.body() ?: throw NetworkAPIException.BodyNullError()
                if (body.isEmpty()) throw NetworkAPIException.EmptyListError()

                emit(body)
            }
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getStopsByRunner(id: Int): Flow<List<BusStop>> = flow {
        try {
            val res = service.getStopsByRunner(id.toString())
            if (res.isSuccessful) {
                val body = res.body() ?: throw NetworkAPIException.BodyNullError()
                if (body.isEmpty()) throw NetworkAPIException.EmptyListError()

                emit(body)
            }
        } catch (e: Exception) {
            throw e
        }
    }
}