package com.joohnq.sppublicbus.common.helper

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth
import org.junit.Before
import org.junit.Test

class UnitsHelperTest {
    private lateinit var context: Context

    @Before
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().context
    }

    @Test
    fun testDpToPxWithValidInput() {
        val result = UnitsHelper.dpToPx(context, 10)
        val expectedValue = (10 * context.resources.displayMetrics.density).toInt()
        Truth.assertThat(result).isEqualTo(expectedValue)
    }

    @Test
    fun testDpToPxWithInvalidInput() {
        val result = UnitsHelper.dpToPx(context, 10)
        val expectedValue = (5 * context.resources.displayMetrics.density).toInt()
        Truth.assertThat(result).isNotEqualTo(expectedValue)
    }
}