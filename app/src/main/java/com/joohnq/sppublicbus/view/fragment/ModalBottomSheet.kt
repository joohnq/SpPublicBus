package com.joohnq.sppublicbus.view.fragment

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.joohnq.sppublicbus.common.helper.RecyclerViewHelper
import com.joohnq.sppublicbus.databinding.ModalBottomSheetBinding
import com.joohnq.sppublicbus.view.adapter.BusLineWithForecastAdapter
import com.joohnq.sppublicbus.view.adapter.BusLinesAdapter
import com.joohnq.sppublicbus.viewmodel.BusLinesViewmodel
import com.joohnq.sppublicbus.viewmodel.ForecastViewmodel
import com.joohnq.sppublicbus.viewmodel.PositionViewmodel

class ModalBottomSheetDialog(
    private val bottomSheet: View,
    private val binding: ModalBottomSheetBinding,
    private val viewLifecycleOwner: LifecycleOwner,
    private val context: Context,
    private val busLinesViewmodel: BusLinesViewmodel,
    private val forecastViewModel: ForecastViewmodel,
    private val positionViewmodel: PositionViewmodel,
) {
    private var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>? = null
    private val busLinesWithForecastAdapter: BusLineWithForecastAdapter by lazy {
        BusLineWithForecastAdapter(context, onClick = onClickOnBusLine)
    }
    private val busLinesAdapter: BusLinesAdapter by lazy {
        BusLinesAdapter(context, onClick = onClickOnBusLine)
    }
    private val onClickOnBusLine = { id: Int ->
        positionViewmodel.getPositionVehiclesByBusLines(id)
        collapse()
    }

    fun initialize() {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet as FrameLayout)
        hidden()
        binding.run {
            observers()
            initRv()
        }
    }

    private fun expandHalf() {
        bottomSheetBehavior?.apply {
            peekHeight = 20
            isHideable = false
            setState(BottomSheetBehavior.STATE_HALF_EXPANDED)
        }
    }

    private fun collapse() {
        bottomSheetBehavior?.apply {
            peekHeight = 10
            isHideable = false
            setState(BottomSheetBehavior.STATE_COLLAPSED)
        }
    }

    fun hidden() {
        bottomSheetBehavior?.apply {
            peekHeight = 0
            isHideable = true
            setState(BottomSheetBehavior.STATE_HIDDEN)
        }
    }

    private fun ModalBottomSheetBinding.handleOnLoadingObserver() {
        expandHalf()
        includeCustomLoadingPage.root.visibility = View.VISIBLE
        includeCustomErrorPage.root.visibility = View.GONE
    }

    private fun ModalBottomSheetBinding.handleOnErrorObserver(error: String) {
        includeCustomLoadingPage.root.visibility = View.GONE
        includeCustomErrorPage.root.visibility = View.VISIBLE
        includeCustomErrorPage.customErrorText.text = error
    }

    private fun ModalBottomSheetBinding.handleOnSuccessObserver() {
        includeCustomLoadingPage.root.visibility = View.GONE
        includeCustomErrorPage.root.visibility = View.GONE
    }

    private fun ModalBottomSheetBinding.observers() {
        busLinesViewmodel.busLinesSearch.observe(viewLifecycleOwner) { state ->
            state.fold(
                onLoading = {
                    handleOnLoadingObserver()
                },
                onError = { handleOnErrorObserver(it) },
                onSuccess = { points ->
                    handleOnSuccessObserver()
                    expandableSearchSectionRv.scrollToPosition(0)
                    expandableSearchSectionRv.adapter = busLinesAdapter
                    busLinesAdapter.submitList(points)
                },
            )
        }
        forecastViewModel.forecastByStop.observe(viewLifecycleOwner) { state ->
            state.fold(
                onLoading = {
                    handleOnLoadingObserver()
                },
                onError = { handleOnErrorObserver(it) },
                onSuccess = { req ->
                    expandHalf()
                    handleOnSuccessObserver()
                    val list = req.p?.l ?: return@fold
                    expandableSearchSectionRv.scrollToPosition(0)
                    expandableSearchSectionRv.adapter = busLinesWithForecastAdapter
                    busLinesWithForecastAdapter.submitList(list)
                },
            )
        }
    }

    private fun ModalBottomSheetBinding.initRv() {
        RecyclerViewHelper.setupRecyclerViewWithoutScroll(
            expandableSearchSectionRv,
            context
        )
    }
}