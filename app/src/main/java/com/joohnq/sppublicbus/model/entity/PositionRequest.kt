package com.joohnq.sppublicbus.model.entity

data class PositionRequest(
    val hr: String,
    val l: List<BusLines>
)