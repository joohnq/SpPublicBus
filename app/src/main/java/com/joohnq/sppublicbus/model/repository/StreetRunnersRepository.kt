package com.joohnq.sppublicbus.model.repository

import com.joohnq.sppublicbus.common.exception.NetworkAPIException
import com.joohnq.sppublicbus.model.entity.StreetRunner
import com.joohnq.sppublicbus.model.services.remote.SpTransService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class StreetRunnersRepository @Inject constructor(
    private val service: SpTransService
) {
    suspend fun getStreetRunners(): Flow<List<StreetRunner>> = flow {
        try {
            val res = service.getRunners()
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