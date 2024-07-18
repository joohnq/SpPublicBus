package com.joohnq.sppublicbus.common.exception

sealed class NetworkException(message: String): Throwable(message = message) {
    data class AuthenticationError(override val message: String = "Error to get authentication"): NetworkException(message = message)
    data class PositionVehicleError(override val message: String = "Error to get position vehicles"): NetworkException(message = message)
    data class BodyNullError(override val message: String = "Body is null"): NetworkException(message = message)
    data class PositionBusLineError(override val message: String = "Error to get bus line position"): NetworkException(message = message)
}