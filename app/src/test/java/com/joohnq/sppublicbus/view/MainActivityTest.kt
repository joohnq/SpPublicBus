package com.joohnq.sppublicbus.view

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import com.joohnq.sppublicbus.R
import com.joohnq.sppublicbus.databinding.ActivityMainBinding
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@LooperMode(LooperMode.Mode.PAUSED)
@Config(
    manifest = Config.NONE,
    sdk = [34],
    application = HiltTestApplication::class,
)
class MainActivityTest {
    @get:Rule val activityRule = ActivityScenarioRule(MainActivity::class.java)
    @get:Rule val hiltAndroidRule = HiltAndroidRule(this)
    private lateinit var activity: MainActivity
    private lateinit var binding: ActivityMainBinding

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        activity = Robolectric.buildActivity(MainActivity::class.java).create().resume().get()
        val shadowActivity = shadowOf(activity)
        binding = ActivityMainBinding.bind(shadowActivity.contentView)
    }

    @Test
    fun testActivity() {
        Truth.assertThat(binding.root).isNotNull()
        onView(withId(R.id.searchButton)).perform(click())
    }
}