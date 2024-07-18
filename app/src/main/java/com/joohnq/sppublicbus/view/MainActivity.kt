package com.joohnq.sppublicbus.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.joohnq.sppublicbus.R
import com.joohnq.sppublicbus.common.helper.RadioButtonHelper
import com.joohnq.sppublicbus.databinding.ActivityMainBinding
import com.joohnq.sppublicbus.databinding.ModalBottomSheetBinding
import com.joohnq.sppublicbus.model.entity.StreetRunner
import com.joohnq.sppublicbus.view.adapter.SearchSpinnerAdapter
import com.joohnq.sppublicbus.view.components.hideKeyboard
import com.joohnq.sppublicbus.view.fragment.ModalBottomSheetDialog
import com.joohnq.sppublicbus.viewmodel.BusLinesViewmodel
import com.joohnq.sppublicbus.viewmodel.ForecastViewmodel
import com.joohnq.sppublicbus.viewmodel.PositionViewmodel
import com.joohnq.sppublicbus.viewmodel.StopsViewmodel
import com.joohnq.sppublicbus.viewmodel.StreetRunnersViewmodel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val context: Context by lazy { this }
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private var selectedStreetRunner: Int = -1
    private val stopsViewmodel: StopsViewmodel by viewModels()
    private val forecastViewModel: ForecastViewmodel by viewModels()
    private val busLinesViewmodel: BusLinesViewmodel by viewModels()
    private val positionViewmodel: PositionViewmodel by viewModels()
    private val streetRunnersViewmodel: StreetRunnersViewmodel by viewModels()
    private val bottomSheetBinding: ModalBottomSheetBinding by lazy {
        binding.includeBottomSheetLayout
    }
    private val bottomSheet: View by lazy {
        bottomSheetBinding.bottomSheetLayout
    }
    private val bottomSheetDialog: ModalBottomSheetDialog by lazy {
        ModalBottomSheetDialog(
            viewLifecycleOwner = this,
            context = context,
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
            this,
            items
        )
    }

    override fun onStop() {
        super.onStop()
        Intent(this, LoadingActivity::class.java).also {
            startActivity(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        binding.run {
            setupInsets()
            observers()
            bindButtons()
        }
        bottomSheetDialog.initialize()
        streetRunnersViewmodel.getStreetRunners()
    }

    private fun ActivityMainBinding.initStreetRunners(streetRunners: List<StreetRunner>) {
        streetRunners.forEach { r ->
            val radio = RadioButtonHelper.createStreetRunnerRadioButton(
                idt = r.cc,
                value = r.nc,
                context = context,
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

    private fun ActivityMainBinding.observers() {
        streetRunnersViewmodel.streetRunners.observe(this@MainActivity) { state ->
            state.fold(
                onSuccess = { req ->
                    initStreetRunners(req)
                },
                onError = {},
                onLoading = {}
            )
        }
    }

    private fun ActivityMainBinding.bindButtons() {
        includeCustomTextInput.apply {
            textInputLayout.setEndIconOnClickListener {
                hideKeyboard()
                textInputEditText.text?.clear()
                textInputEditText.clearFocus()
                bottomSheetDialog.hidden()
                positionViewmodel.setPositionVehicles()
            }
            searchButton.setOnClickListener {
                val text = textInputEditText.text.toString()
                if (text.isEmpty()) return@setOnClickListener

                hideKeyboard()
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

    private fun ActivityMainBinding.setupInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            initToolbar(systemBars.top)
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun ActivityMainBinding.initToolbar(top: Int) {
        includeCustomTextInput.root.layoutParams =
            (includeCustomTextInput.root.layoutParams as ViewGroup.MarginLayoutParams).apply {
                topMargin = top
            }
        includeCustomTextInput.spinner.adapter = customSearchSpinnerAdapter
    }
}