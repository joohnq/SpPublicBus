package com.joohnq.sppublicbus.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.joohnq.sppublicbus.databinding.CustomBusStopItemBinding
import com.joohnq.sppublicbus.model.entity.BusStop
import com.joohnq.sppublicbus.view.adapter.diffutil.BusPointsRequestItemDiffCallback

class BusPointsAdapter(val context: Context, val onClick: (Int) -> Unit) : Adapter<BusPointsAdapter.BusPointsAdapterViewHolder>() {
    private val differ = AsyncListDiffer(this, BusPointsRequestItemDiffCallback())

    inner class BusPointsAdapterViewHolder(val binding: CustomBusStopItemBinding) :
        ViewHolder(binding.root) {
        fun bind(busStop: BusStop) {
            binding.apply {
                tvNome.text = busStop.np
                tvEndereco.text = busStop.ed

                busCard.setOnClickListener {
                    onClick(busStop.cp)
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusPointsAdapterViewHolder =
        BusPointsAdapterViewHolder(
            CustomBusStopItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    override fun getItemCount() = differ.currentList.size

    fun submitList(list: List<BusStop>) {
        differ.submitList(list)
    }

    override fun onBindViewHolder(holder: BusPointsAdapterViewHolder, position: Int) =
        holder.bind(differ.currentList[position])
}