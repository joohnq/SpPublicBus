package com.joohnq.sppublicbus.common.helper

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth
import org.junit.Before
import org.junit.Test

class RecyclerViewHelperTest {
    private lateinit var context: Context
    private lateinit var recyclerView: RecyclerView

    @Before
    fun setUp(){
        context = ApplicationProvider.getApplicationContext()
        recyclerView = RecyclerView(context)
    }

    @Test
    fun testSetupRecyclerViewWithoutScroll() {
        val recycler = RecyclerViewHelper.setupRecyclerViewWithoutScroll(recyclerView, context)
        Truth.assertThat(recycler).isNotNull()
        Truth.assertThat(recycler.isVerticalScrollBarEnabled).isFalse()
    }
}

