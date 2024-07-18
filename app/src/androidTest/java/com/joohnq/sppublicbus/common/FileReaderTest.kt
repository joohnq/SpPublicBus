package com.joohnq.sppublicbus.common

import com.google.common.truth.Truth

import org.junit.Test

class FileReaderTest {
    @Test
    fun testReadStringFromFileWithValidFile() {
        val str = FileReader.readStringFromFile("forecast_with_lines_empty.json")
        Truth.assertThat(str).isNotEmpty()
        Truth.assertThat(str).isNotNull()
        Truth.assertThat(str.trimIndent())
            .isEqualTo("""{"hr": "17:31", "p": {"cp": 3305795, "np": "CAY B/C", "py": -23.574436, "px": -46.603153, "l": []}}""")
    }

    @Test(expected = Exception::class)
    fun testReadStringFromFileWithInvalidFile() {
        FileReader.readStringFromFile("")
    }
}