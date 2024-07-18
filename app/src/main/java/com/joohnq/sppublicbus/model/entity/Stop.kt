package com.joohnq.sppublicbus.model.entity

data class Stop(
    val cp: Int,
    val l: List<BusLines>,
    val np: String,
    val px: Double,
    val py: Double
)