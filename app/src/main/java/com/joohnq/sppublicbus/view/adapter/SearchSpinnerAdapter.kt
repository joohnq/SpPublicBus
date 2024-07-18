package com.joohnq.sppublicbus.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.joohnq.sppublicbus.R
import com.joohnq.sppublicbus.databinding.CustomSearchOptionBinding
import com.joohnq.sppublicbus.databinding.CustomSearchOptionSelectedBinding

class SearchSpinnerAdapter(
    private val context: Context,
    private val items: Map<String, Int>,
) : ArrayAdapter<String?>(context, 0, items.map { it.key }) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemViewDropDown(position, convertView, parent)
    }

    private fun createItemViewDropDown(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = if (convertView == null) {
            CustomSearchOptionBinding.inflate(LayoutInflater.from(context), parent, false)
        } else {
            CustomSearchOptionBinding.bind(convertView)
        }

        binding.apply {
            root.backgroundTintList = context.getColorStateList(R.color.white)
            option.text = items.keys.toList()[position]
            icon.setImageResource(items.values.toList()[position])
        }

        return binding.root
    }

    private fun createItemView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = if (convertView == null) {
            CustomSearchOptionSelectedBinding.inflate(LayoutInflater.from(context), parent, false)
        } else {
            CustomSearchOptionSelectedBinding.bind(convertView)
        }

        binding.icon.setImageResource(items.values.toList()[position])
        return binding.root
    }
}
