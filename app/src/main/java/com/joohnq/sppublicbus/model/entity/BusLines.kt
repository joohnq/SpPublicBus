package com.joohnq.sppublicbus.model.entity

data class BusLines(
    val c: String,
    val cl: Int,
    val lt0: String,
    val lt1: String,
    val qv: Int,
    val sl: Int,
    val vs: List<VehicleWithForecast>
)