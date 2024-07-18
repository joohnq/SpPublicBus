package com.joohnq.sppublicbus.common.exception

sealed class NetworkAPIException(message: String) : Throwable(message = message) {
    data class AuthenticationError(override val message: String = "Error to get authentication") :
        NetworkAPIException(message = message)

    data class PositionVehicleError(override val message: String = "Error to get position vehicles") :
        NetworkAPIException(message = message)

    data class BodyNullError(override val message: String = "Body is null") :
        NetworkAPIException(message = message)

    data class PositionBusLineError(override val message: String = "Error to get bus line position") :
        NetworkAPIException(message = message)

    data class EmptyListError(override val message: String = "List is empty") :
        NetworkAPIException(message = message)
}