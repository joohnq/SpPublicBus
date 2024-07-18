package com.joohnq.sppublicbus.view.entity

data class CustomMarker<T>(
    val la: Double,
    val lo: Double,
    val title: String,
    val icon: Int,
    val item: T,
)