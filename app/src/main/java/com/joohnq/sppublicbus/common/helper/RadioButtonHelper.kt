package com.joohnq.sppublicbus.common.helper

import android.content.Context
import android.widget.RadioButton
import android.widget.RadioGroup
import com.joohnq.sppublicbus.R

object RadioButtonHelper {
    fun createStreetRunnerRadioButton(
        idt: Int,
        value: String,
        context: Context,
        onRadioClick: (Int) -> Unit,
    ): RadioButton {
        return RadioButton(context).apply {
            val paddingHorizontal = UnitsHelper.dpToPx(context, 10)
            id = idt
            text = value
            buttonDrawable = null
            setPadding(paddingHorizontal, 0, paddingHorizontal, 0)
            setBackgroundResource(R.drawable.shape_street_runner_selector)
            setTextAppearance(R.style.radioButtonStreetRunner)
            val params = RadioGroup.LayoutParams(
                RadioGroup.LayoutParams.WRAP_CONTENT,
                RadioGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(UnitsHelper.dpToPx(context, 5), 0, 0, 0)
            }
            layoutParams = params
        }.apply {
            setOnClickListener {
                onRadioClick(idt)
            }
        }
    }
}