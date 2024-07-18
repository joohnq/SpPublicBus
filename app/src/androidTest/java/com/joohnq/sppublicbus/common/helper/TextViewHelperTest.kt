package com.joohnq.sppublicbus.common.helper

import android.content.Context
import android.widget.LinearLayout
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth
import org.junit.Before
import org.junit.Test

class TextViewHelperTest {
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().context
    }

    @Test
    fun testCreateBusLineWithForecast() {
        val value = "Value"
        val textView = TextViewHelper.createBusLineWithForecast(value, context)

        Truth.assertThat(textView).isNotNull()
        Truth.assertThat(textView.text).isEqualTo(value)

        val params = textView.layoutParams as LinearLayout.LayoutParams
        val expectedMarginEnd = UnitsHelper.dpToPx(context, 5)
        Truth.assertThat(params.marginEnd).isEqualTo(expectedMarginEnd)
    }
}