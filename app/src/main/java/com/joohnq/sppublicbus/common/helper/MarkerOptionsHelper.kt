package com.joohnq.sppublicbus.common.helper

import android.content.Context
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.joohnq.sppublicbus.view.entity.CustomMarker

object MarkerOptionsHelper {
    fun createCustomMarkerOptions(
        customMarker: CustomMarker<*>,
        context: Context
    ): MarkerOptions {
        return MarkerOptions()
            .position(LatLng(customMarker.la, customMarker.lo))
            .title(customMarker.title)
            .icon(
                BitmapHelper.vectorToBitmap(
                    context,
                    customMarker.icon,
                )
            )
    }
}