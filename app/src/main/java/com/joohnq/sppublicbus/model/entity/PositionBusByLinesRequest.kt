package com.joohnq.sppublicbus.model.entity

data class PositionBusByLinesRequest(
    val hr: String,
    val vs: List<Vehicle>
)