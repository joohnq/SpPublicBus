package com.joohnq.sppublicbus.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.material.internal.ViewUtils.hideKeyboard
import com.google.android.material.snackbar.Snackbar
import com.joohnq.sppublicbus.R
import com.joohnq.sppublicbus.common.helper.RadioButtonHelper
import com.joohnq.sppublicbus.databinding.ActivityMainBinding
import com.joohnq.sppublicbus.databinding.FragmentHomeBinding
import com.joohnq.sppublicbus.databinding.ModalBottomSheetBinding
import com.joohnq.sppublicbus.model.entity.BusStop
import com.joohnq.sppublicbus.model.entity.StreetRunner
import com.joohnq.sppublicbus.model.entity.Vehicle
import com.joohnq.sppublicbus.model.entity.toCustomMarker
import com.joohnq.sppublicbus.view.GoogleMapHelper
import com.joohnq.sppublicbus.view.adapter.SearchSpinnerAdapter
import com.joohnq.sppublicbus.view.components.hideKeyboard
import com.joohnq.sppublicbus.viewmodel.BusLinesViewmodel
import com.joohnq.sppublicbus.viewmodel.ForecastViewmodel
import com.joohnq.sppublicbus.viewmodel.PositionViewmodel
import com.joohnq.sppublicbus.viewmodel.StopsViewmodel
import com.joohnq.sppublicbus.viewmodel.StreetRunnersViewmodel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var selectedStreetRunner: Int = -1
    private val positionViewmodel: PositionViewmodel by activityViewModels()
    private val stopsViewmodel: StopsViewmodel by activityViewModels()
    private val forecastViewmodel: ForecastViewmodel by activityViewModels()
    private val streetRunnersViewmodel: StreetRunnersViewmodel by activityViewModels()
    private val forecastViewModel: ForecastViewmodel by activityViewModels()
    private val busLinesViewmodel: BusLinesViewmodel by activityViewModels()
    private val googleMapHelper: GoogleMapHelper by lazy {
        GoogleMapHelper(
            context = requireContext(),
            observers = ::observers,
            onGetForecastByStop = forecastViewmodel::getForecastByStop
        )
    }
    private val bottomSheetBinding: ModalBottomSheetBinding by lazy {
        binding.includeBottomSheetLayout
    }
    private val bottomSheet: View by lazy {
        bottomSheetBinding.bottomSheetLayout
    }
    private val bottomSheetDialog: ModalBottomSheetDialog by lazy {
        ModalBottomSheetDialog(
            viewLifecycleOwner = this,
            context = requireContext(),
            bottomSheet = bottomSheet,
            busLinesViewmodel = busLinesViewmodel,
            forecastViewModel = forecastViewModel,
            positionViewmodel = positionViewmodel,
            onClearStreetRunnersSelection = { binding.streetRunnersRadioGroup.clearCheck() },
            binding = bottomSheetBinding
        )
    }
    private val customSearchSpinnerAdapter: SearchSpinnerAdapter by lazy {
        val items = mapOf(
            getString(R.string.bus_lines) to R.drawable.ic_bus_line,
            getString(R.string.stops) to R.drawable.ic_stop
        )
        SearchSpinnerAdapter(
            requireContext(),
            items
        )
    }

    private val onNone = {
        googleMapHelper.clear()
        googleMapHelper.setMainCamera()
    }

    private val onError = { error: String ->
        Snackbar.make(binding.root, error, Snackbar.LENGTH_SHORT).show()
    }

    private val onSuccess = {
        googleMapHelper.clear()
    }

    private fun FragmentHomeBinding.initStreetRunners(streetRunners: List<StreetRunner>) {
        streetRunners.forEach { r ->
            val radio = RadioButtonHelper.createStreetRunnerRadioButton(
                idt = r.cc,
                value = r.nc,
                context = requireContext(),
                onRadioClick = { id ->
                    bottomSheetDialog.hidden()
                    if (id == selectedStreetRunner) {
                        selectedStreetRunner = -1
                        streetRunnersRadioGroup.clearCheck()
                        stopsViewmodel.setBusPointsSearchNone()
                    } else {
                        selectedStreetRunner = id
                        stopsViewmodel.getStopsByRunner(id)
                    }
                }
            )
            streetRunnersRadioGroup.addView(radio)
        }
    }

    private fun FragmentHomeBinding.bindButtons() {
        includeCustomTextInput.apply {
            textInputLayout.setEndIconOnClickListener {
                requireActivity().hideKeyboard()
                textInputEditText.text?.clear()
                textInputEditText.clearFocus()
                bottomSheetDialog.hidden()
                positionViewmodel.setPositionVehicles()
            }
            searchButton.setOnClickListener {
                val text = textInputEditText.text.toString()
                if (text.isEmpty()) return@setOnClickListener

                requireActivity().hideKeyboard()
                streetRunnersRadioGroup.clearCheck()

                when (includeCustomTextInput.spinner.selectedItemPosition) {
                    0 -> busLinesViewmodel.getBusLines(text)
                    1 -> {
                        bottomSheetDialog.hidden()
                        stopsViewmodel.getBusPoints(text)
                    }

                    else -> return@setOnClickListener
                }
            }
        }
    }

    private fun FragmentHomeBinding.initToolbar() {
        includeCustomTextInput.spinner.adapter = customSearchSpinnerAdapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bindButtons()
        initMapView()
        bottomSheetDialog.initialize()
        streetRunnersViewmodel.getStreetRunners()
        observers()
        binding.bindButtons()
        binding.initToolbar()
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
                    googleMapHelper.addMarkers(
                        req.vs,
                        addMarker = { marker: Vehicle ->
                            val customMarker = marker.toCustomMarker()
                            googleMapHelper.addMarker(customMarker)
                        },
                    )

                },
                onError = onError
            )
        }
        stopsViewmodel.busPointsSearch.observe(viewLifecycleOwner) { state ->
            state.fold(
                onNone = onNone,
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
        streetRunnersViewmodel.streetRunners.observe(viewLifecycleOwner) { state ->
            state.fold(
                onSuccess = { req ->
                    binding.initStreetRunners(req)
                },
                onError = {},
                onLoading = {}
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
