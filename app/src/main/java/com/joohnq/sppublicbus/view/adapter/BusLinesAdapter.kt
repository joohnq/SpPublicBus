package com.joohnq.sppublicbus.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.joohnq.sppublicbus.R
import com.joohnq.sppublicbus.databinding.CustomBusLineItemBinding
import com.joohnq.sppublicbus.model.entity.BusLinesRequestItem
import com.joohnq.sppublicbus.view.adapter.diffutil.BusLinesRequestItemDiffCallback

class BusLinesAdapter(val context: Context, val onClick: (Int) -> Unit) : Adapter<BusLinesAdapter.BusLinesAdapterViewHolder>() {
    private val differ = AsyncListDiffer(this, BusLinesRequestItemDiffCallback())

    inner class BusLinesAdapterViewHolder(val binding: CustomBusLineItemBinding) :
        ViewHolder(binding.root) {
        fun bind(busLinesRequestItem: BusLinesRequestItem) {
            binding.apply {
                txLetreiroNumero.text = context.getString(
                    R.string.letreiro_numero,
                    busLinesRequestItem.lt,
                    busLinesRequestItem.tl
                )
                tvDestino.text = if(busLinesRequestItem.sl == 1) busLinesRequestItem.tp else busLinesRequestItem.ts
                tvTerminalSecundarioPrincipal.text = context.getString(
                    R.string.terminal_s_p,
                    busLinesRequestItem.ts,
                    busLinesRequestItem.tp
                )
                busCard.setOnClickListener {
                    onClick(busLinesRequestItem.cl)
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusLinesAdapterViewHolder =
        BusLinesAdapterViewHolder(
            CustomBusLineItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    override fun getItemCount() = differ.currentList.size

    fun submitList(list: List<BusLinesRequestItem>) {
        differ.submitList(list)
    }

    override fun onBindViewHolder(holder: BusLinesAdapterViewHolder, position: Int) =
        holder.bind(differ.currentList[position])
}