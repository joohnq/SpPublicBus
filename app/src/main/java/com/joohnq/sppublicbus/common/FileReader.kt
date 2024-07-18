package com.joohnq.sppublicbus.common

import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.android.testing.HiltTestApplication
import java.io.IOException
import java.io.InputStreamReader

object FileReader {
    fun readStringFromFile(file: String): String {
        try {
            val context =
                InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as HiltTestApplication
            println("ContextAssets:" + context.assets)
            val inputStream = context.assets.open(file)
            val builder = StringBuilder()
            val reader = InputStreamReader(inputStream, "UTF-8")
            reader.readLines().forEach {
                builder.append(it)
            }
            return builder.toString()
        } catch (e: IOException) {
            throw e
        }
    }
}