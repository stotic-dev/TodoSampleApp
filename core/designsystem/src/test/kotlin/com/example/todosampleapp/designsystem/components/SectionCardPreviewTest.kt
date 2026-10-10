package com.example.todosampleapp.designsystem.components

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.RoborazziRule
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

/**
 * SectionCard のスナップショットテスト。
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel7)
class SectionCardPreviewTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val roborazziRule =
        RoborazziRule(
            options =
                RoborazziRule.Options(
                    outputDirectoryPath = "src/test/screenshots",
                ),
        )

    @Test
    fun todoDetailPreviewTest() {
        composeTestRule.setContent {
            SectionCardPreview()
        }
        composeTestRule.onRoot().captureRoboImage()
    }
}
