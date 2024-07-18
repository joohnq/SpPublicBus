package com.joohnq.sppublicbus.common.helper

import android.content.Context
import android.view.ViewGroup.LayoutParams
import android.widget.LinearLayout
import android.widget.TextView
import com.joohnq.sppublicbus.R

object TextViewHelper {
    fun createBusLineWithForecast(
        value: String,
        context: Context,
    ): TextView {
        return TextView(
            context,
            null,
            R.style.textViewBusLineWithForecast,
            R.style.textViewBusLineWithForecast
        ).apply {
            val params: LinearLayout.LayoutParams =
                LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
                    .apply {
                        setMargins(0, 0, UnitsHelper.dpToPx(context, 5), 0)
                    }
            text = value
            layoutParams = params
            setTextAppearance(R.style.textViewBusLineWithForecastTextStyle)
        }
    }
}