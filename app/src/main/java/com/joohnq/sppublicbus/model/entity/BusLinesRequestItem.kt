package com.joohnq.sppublicbus.model.entity

data class BusLinesRequestItem(
    val cl: Int,
    val lc: Boolean,
    val lt: String,
    val sl: Int,
    val tl: Int,
    val tp: String,
    val ts: String
)