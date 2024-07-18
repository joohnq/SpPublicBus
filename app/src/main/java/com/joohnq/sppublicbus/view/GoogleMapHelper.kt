package com.joohnq.sppublicbus.view

import android.content.Context
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.joohnq.sppublicbus.common.helper.MarkerOptionsHelper
import com.joohnq.sppublicbus.model.entity.BusStop
import com.joohnq.sppublicbus.view.adapter.MarkerInfoAdapter
import com.joohnq.sppublicbus.view.entity.CustomMarker

class GoogleMapHelper(
    private val context: Context,
    private val observers: () -> Unit,
    private val onGetForecastByStop: (Int) -> Unit
) : OnMapReadyCallback {
    private lateinit var map: GoogleMap
    private val saoPaulo = LatLng(-23.5505, -46.6333)

    private val setOnMarkerClickListener: GoogleMap.OnMarkerClickListener =
        GoogleMap.OnMarkerClickListener { marker: Marker ->
            val tag = marker.tag as? CustomMarker<*> ?: return@OnMarkerClickListener false
            val item = tag.item as? BusStop ?: return@OnMarkerClickListener false
            marker.showInfoWindow()
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.position, 15f))
            onGetForecastByStop(item.cp)
            false
        }

    fun clear() {
        if (::map.isInitialized) {
            map.clear()
        }
    }

    fun addMarker(customMarker: CustomMarker<*>): Marker? {
        if (::map.isInitialized) {
            val marker = map.addMarker(
                MarkerOptionsHelper.createCustomMarkerOptions(customMarker, context)
            )
            marker?.tag = customMarker
            return marker
        }
        return null
    }

    fun <T> addMarkers(items: List<T>, addMarker: (T) -> Marker?, onFinish: () -> Unit = {}) {
        if (::map.isInitialized) {
            val boundsBuilder = LatLngBounds.Builder()
            items.forEach { item ->
                val marker = addMarker(item) ?: return@forEach
                boundsBuilder.include(marker.position)
            }
            val bounds = boundsBuilder.build()
            val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 100)
            map.animateCamera(cameraUpdate)
            onFinish()
        }
    }

    fun setMainCamera() {
        if (::map.isInitialized) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(saoPaulo, 10f))
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.run {
            setMainCamera()
            setInfoWindowAdapter(MarkerInfoAdapter(context))
            setOnMarkerClickListener(setOnMarkerClickListener)
            observers()
        }
    }
}