package com.example.todosampleapp.feature.todoadd.ui

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.todosampleapp.designsystem.theme.AppTheme
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.RoborazziRule
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

/**
 * TodoAddScreen のスナップショットテスト。
 *
 * - 記録: ./gradlew :feature:todoadd:ui:recordRoborazziDebug
 * - 検証: ./gradlew :feature:todoadd:ui:verifyRoborazziDebug
 * - 差分: ./gradlew :feature:todoadd:ui:compareRoborazziDebug
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel7)
class TodoAddScreenshotTest {
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
    fun todoAdd_filled() {
        composeTestRule.setContent {
            // dynamicColor を無効化して端末の壁紙色に依存しないようにする
            AppTheme(dynamicColor = false) {
                TodoAddScreen(
                    title = "牛乳を買う",
                    detail = "低脂肪のもの",
                    canSave = true,
                    onClickSaveButton = {},
                    onCancel = {},
                    onValueChangeTitle = {},
                    onValueChangeDetail = {},
                )
            }
        }
        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    fun todoAdd_empty() {
        composeTestRule.setContent {
            AppTheme(dynamicColor = false) {
                TodoAddScreen(
                    title = "",
                    detail = "",
                    canSave = false,
                    onClickSaveButton = {},
                    onCancel = {},
                    onValueChangeTitle = {},
                    onValueChangeDetail = {},
                )
            }
        }
        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    fun todoAdd_titleOnly() {
        composeTestRule.setContent {
            AppTheme(dynamicColor = false) {
                TodoAddScreen(
                    title = "牛乳を買う",
                    detail = "",
                    canSave = false,
                    onClickSaveButton = {},
                    onCancel = {},
                    onValueChangeTitle = {},
                    onValueChangeDetail = {},
                )
            }
        }
        composeTestRule.onRoot().captureRoboImage()
    }
}
