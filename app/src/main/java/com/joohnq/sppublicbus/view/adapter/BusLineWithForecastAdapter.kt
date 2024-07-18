package com.joohnq.sppublicbus.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.chip.Chip
import com.joohnq.sppublicbus.R
import com.joohnq.sppublicbus.common.helper.TextViewHelper
import com.joohnq.sppublicbus.databinding.CustomBusLineWithForecastItemBinding
import com.joohnq.sppublicbus.model.entity.BusLines
import com.joohnq.sppublicbus.view.adapter.diffutil.BusLinesItemDiffCallback

class BusLineWithForecastAdapter(val context: Context, val onClick: (Int) -> Unit) :
    Adapter<BusLineWithForecastAdapter.BusLineWithForecastAdapterViewHolder>() {
    private val differ = AsyncListDiffer(this, BusLinesItemDiffCallback())

    inner class BusLineWithForecastAdapterViewHolder(val binding: CustomBusLineWithForecastItemBinding) :
        ViewHolder(binding.root) {
        fun bind(busLine: BusLines) {
            binding.apply {
                val vehicles = busLine.vs
                txLetreiro.text = busLine.c
                val start: String = if (busLine.sl == 1) busLine.lt0 else busLine.lt1
                val end: String = if (busLine.sl == 1) busLine.lt1 else busLine.lt0
                tvDestino.text = end

                vehicles.forEach { v ->
                    val id = v.p
                    val hour = v.t
                    val textView = TextViewHelper.createBusLineWithForecast(
                        value = context.getString(R.string.forecast_text, id, hour),
                        context = context,
                    )
                    vehiclesByStop.addView(textView)
                }

                tvTerminalSecundarioPrincipal.text =
                    context.getString(R.string.terminal_s_p, start, end)
                busCard.setOnClickListener {
                    onClick(busLine.cl)
                }
            }
        }

        private fun createChip(id: String, hour: String): Chip {
            return Chip(context).apply {
                text = context.getString(R.string.forecast_text, id, hour)
                isCloseIconVisible = false
                chipBackgroundColor = context.getColorStateList(R.color.primary)
                chipStrokeWidth = 0f
                chipMinHeight = 60f
                setTextAppearance(R.style.chip)
            }
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BusLineWithForecastAdapterViewHolder =
        BusLineWithForecastAdapterViewHolder(
            CustomBusLineWithForecastItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    override fun getItemCount() = differ.currentList.size

    fun submitList(list: List<BusLines>) {
        differ.submitList(list)
    }

    override fun onBindViewHolder(holder: BusLineWithForecastAdapterViewHolder, position: Int) =
        holder.bind(differ.currentList[position])
}