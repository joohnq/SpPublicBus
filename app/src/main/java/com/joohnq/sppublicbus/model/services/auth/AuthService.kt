package com.joohnq.sppublicbus.model.services.auth

import com.joohnq.sppublicbus.common.Constants.API_PATH_AUTHENTICATION
import retrofit2.Response
import retrofit2.http.POST

interface AuthService {
    @POST(API_PATH_AUTHENTICATION)
    suspend fun authentication(): Response<Boolean>
}