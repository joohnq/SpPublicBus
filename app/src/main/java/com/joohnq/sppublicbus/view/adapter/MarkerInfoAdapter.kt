package com.joohnq.sppublicbus.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.joohnq.sppublicbus.R
import com.joohnq.sppublicbus.databinding.CurtomMarkerInfoBinding
import com.joohnq.sppublicbus.model.entity.Vehicle
import com.joohnq.sppublicbus.view.entity.CustomMarker

class MarkerInfoAdapter(private val context: Context) : GoogleMap.InfoWindowAdapter {
    private fun createItemView(marker: Marker): View? {
        val customMarker = marker.tag as? CustomMarker<*> ?: return null
        val item = customMarker.item as? Vehicle ?: return null
        val layoutInflater = LayoutInflater.from(context).inflate(R.layout.curtom_marker_info, null)
        val binding = CurtomMarkerInfoBinding.bind(layoutInflater)
        val isAccessible = context.getString(if (item.a) R.string.yes else R.string.no)
        binding.prefixo.text = context.getString(R.string.vehicle_prefix, item.p)
        binding.acessivelParaPCD.text = context.getString(R.string.is_accessible, isAccessible)

        return layoutInflater
    }

    override fun getInfoContents(marker: Marker): View? {
        return createItemView(marker)
    }

    override fun getInfoWindow(marker: Marker): View? {
        return createItemView(marker)
    }
}