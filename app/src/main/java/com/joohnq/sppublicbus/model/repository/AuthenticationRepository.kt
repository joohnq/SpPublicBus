package com.joohnq.sppublicbus.model.repository

import com.joohnq.sppublicbus.common.exception.NetworkAPIException
import com.joohnq.sppublicbus.model.services.auth.AuthService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthenticationRepository @Inject constructor(
    private val service: AuthService
) {
    suspend fun authentication(): Flow<Boolean> = flow {
        try {
            val res = service.authentication()
            if(res.code() != 401){
                val body = res.body() ?: throw NetworkAPIException.BodyNullError()
                if (!body) throw NetworkAPIException.AuthenticationError()

                emit(true)
            }
        }catch (e: Exception){
            throw e
        }
    }
}
