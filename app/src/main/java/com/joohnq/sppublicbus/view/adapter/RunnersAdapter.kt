package com.joohnq.sppublicbus.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.joohnq.sppublicbus.databinding.CustomStreetRunnerItemBinding
import com.joohnq.sppublicbus.model.entity.StreetRunner
import com.joohnq.sppublicbus.view.adapter.diffutil.RunnersRequestItemDiffCallback

class RunnersAdapter(val context: Context, val onClick: (Int) -> Unit) : Adapter<RunnersAdapter.RunnersAdapterViewHolder>() {
    private val differ = AsyncListDiffer(this, RunnersRequestItemDiffCallback())

    inner class RunnersAdapterViewHolder(val binding: CustomStreetRunnerItemBinding) :
        ViewHolder(binding.root) {
        fun bind(streetRunner: StreetRunner) {
            binding.apply {
                tvNome.text = streetRunner.nc

                busCard.setOnClickListener {
                    onClick(streetRunner.cc)
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunnersAdapterViewHolder =
        RunnersAdapterViewHolder(
            CustomStreetRunnerItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    override fun getItemCount() = differ.currentList.size

    fun submitList(list: List<StreetRunner>) {
        differ.submitList(list)
    }

    override fun onBindViewHolder(holder: RunnersAdapterViewHolder, position: Int) =
        holder.bind(differ.currentList[position])
}