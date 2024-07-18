package com.joohnq.sppublicbus.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.material.snackbar.Snackbar
import com.joohnq.sppublicbus.R
import com.joohnq.sppublicbus.databinding.FragmentHomeBinding
import com.joohnq.sppublicbus.model.entity.BusStop
import com.joohnq.sppublicbus.model.entity.Vehicle
import com.joohnq.sppublicbus.model.entity.toCustomMarker
import com.joohnq.sppublicbus.view.GoogleMapHelper
import com.joohnq.sppublicbus.viewmodel.ForecastViewmodel
import com.joohnq.sppublicbus.viewmodel.PositionViewmodel
import com.joohnq.sppublicbus.viewmodel.StopsViewmodel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val positionViewmodel: PositionViewmodel by activityViewModels()
    private val stopsViewmodel: StopsViewmodel by activityViewModels()
    private val forecastViewmodel: ForecastViewmodel by activityViewModels()
    private val googleMapHelper: GoogleMapHelper by lazy {
        GoogleMapHelper(
            context = requireContext(),
            observers = { observers() },
            onGetForecastByStop = forecastViewmodel::getForecastByStop
        )
    }

    private val onNone = {
        googleMapHelper.clear()
        googleMapHelper.setMainCamera()
    }

    private val onLoading = {
        binding.toggleReloadButton(false)
    }

    private val onError = { error: String ->
        binding.toggleLoadingState(false)
        Snackbar.make(binding.root, error, Snackbar.LENGTH_SHORT).show()
    }

    private val onSuccess = {
        googleMapHelper.clear()
        binding.toggleLoadingState(false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bindButtons()
        initMapView()
    }

    private fun FragmentHomeBinding.bindButtons() {
        reloadButton.setOnClickListener {
            positionViewmodel.getPositionVehiclesByBusLines()
        }
    }

    private fun FragmentHomeBinding.toggleReloadButton(state: Boolean) {
        reloadButton.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun FragmentHomeBinding.toggleLoadingState(state: Boolean) {
        includeCustomLoadingPage.visibility = if (state) View.VISIBLE else View.GONE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun observers() {
        positionViewmodel.positionVehicles.observe(viewLifecycleOwner) { state ->
            state.fold(
                onNone = onNone,
                onSuccess = { req ->
                    onSuccess()
                    binding.reloadButton.text = getString(R.string.latest_update, req.hr)
                    googleMapHelper.addMarkers(
                        req.vs,
                        addMarker = { marker: Vehicle ->
                            val customMarker = marker.toCustomMarker()
                            googleMapHelper.addMarker(customMarker)
                        },
                        onFinish = {
                            binding.toggleReloadButton(true)
                        }
                    )

                },
                onLoading = {
                    onLoading()
                    binding.reloadButton.text =
                        getString(R.string.latest_update, getString(R.string.loading_dots))
                },
                onError = onError
            )
        }
        stopsViewmodel.busPointsSearch.observe(viewLifecycleOwner) { state ->
            state.fold(
                onNone = onNone,
                onLoading = onLoading,
                onError = onError,
                onSuccess = { req: List<BusStop> ->
                    onSuccess()
                    googleMapHelper.addMarkers(
                        req,
                        addMarker = { busStop: BusStop ->
                            val customMarker = busStop.toCustomMarker()
                            googleMapHelper.addMarker(customMarker)
                        }
                    )
                },
            )
        }
    }

    private fun initMapView() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment?
        mapFragment?.getMapAsync(googleMapHelper)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
