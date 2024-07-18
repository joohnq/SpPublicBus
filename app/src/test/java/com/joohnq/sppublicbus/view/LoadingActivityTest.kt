package com.joohnq.sppublicbus.view

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.joohnq.sppublicbus.R
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
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
class LoadingActivityTest {
    @get:Rule val activityRule = ActivityScenarioRule(MainActivity::class.java)
    @get:Rule val hiltAndroidRule = HiltAndroidRule(this)
    private lateinit var activity: LoadingActivity

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        activity = Robolectric.buildActivity(LoadingActivity::class.java).create().resume().get()
    }

    @Test
    fun testActivity() {
        onView(withId(R.id.includeCustomLoadingPage)).check(matches(isDisplayed()))
    }
}