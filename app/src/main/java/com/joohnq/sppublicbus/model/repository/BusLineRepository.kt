package com.joohnq.sppublicbus.model.repository

import com.joohnq.sppublicbus.common.exception.NetworkAPIException
import com.joohnq.sppublicbus.model.entity.BusLinesRequestItem
import com.joohnq.sppublicbus.model.services.remote.SpTransService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BusLineRepository @Inject constructor(
    private val service: SpTransService
) {
    suspend fun getBusLines(search: String): Flow<List<BusLinesRequestItem>> = flow {
        try {
            val res = service.getBusLines(search)

            if (res.isSuccessful) {
                val body = res.body() ?: throw NetworkAPIException.BodyNullError()
                if (body.isEmpty()) throw NetworkAPIException.EmptyListError()
                emit(body)
            }
        }catch (e: Exception){
            println("getBusLines: Exception $e")
        }
    }
}