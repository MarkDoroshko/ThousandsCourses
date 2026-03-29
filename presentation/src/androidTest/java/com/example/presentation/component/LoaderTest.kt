package com.example.presentation.component

import androidx.activity.ComponentActivity
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.presentation.theme.ThousandsCoursesTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoaderTest {
    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun loader_visible_when_displayed() {
        rule.setContent {
            ThousandsCoursesTheme {
                Loader()
            }
        }

        rule.onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate)).assertIsDisplayed()
    }
}
