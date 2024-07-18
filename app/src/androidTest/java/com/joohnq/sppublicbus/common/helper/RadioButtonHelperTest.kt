package com.joohnq.sppublicbus.common.helper

import android.content.Context
import android.widget.LinearLayout
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth
import org.junit.Before
import org.junit.Test

class RadioButtonHelperTest {
    private lateinit var context: Context
    private val id = 1
    private val value = "Value"
    private var clickedId = -1
    private val onRadioClick: (Int) -> Unit = { clickedId = it }

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun testCreateStreetRunnerRadioButton() {
        val radioButton =
            RadioButtonHelper.createStreetRunnerRadioButton(
                idt = id,
                value = value,
                context = context,
                onRadioClick = onRadioClick
            )

        Truth.assertThat(radioButton).isNotNull()
        Truth.assertThat(radioButton.id).isEqualTo(id)
        Truth.assertThat(radioButton.text).isEqualTo(value)
        Truth.assertThat(radioButton.buttonDrawable).isNull()

        val params = radioButton.layoutParams as LinearLayout.LayoutParams
        val expectedMarginStart = UnitsHelper.dpToPx(context, 5)
        Truth.assertThat(params.marginStart).isEqualTo(expectedMarginStart)

        radioButton.performClick()
        Truth.assertThat(clickedId).isEqualTo(id)
    }
}