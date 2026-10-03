package com.example.todosampleapp.feature.tododetail.ui

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.todosampleapp.designsystem.theme.AppTheme
import com.example.todosampleapp.domain.todo.TodoItem
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.RoborazziRule
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

/**
 * TodoDetailScreen のスナップショットテスト。
 *
 * - 記録: ./gradlew :feature:TODODETAILi:recordRoborazziDebug
 * - 検証: ./gradlew :feature:TODODETAILi:verifyRoborazziDebug
 * - 差分: ./gradlew :feature:TODODETAILi:compareRoborazziDebug
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel7)
class TodoDetailScreenshotTest {
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
    fun todoDetail_loaded() {
        composeTestRule.setContent {
            // dynamicColor を無効化して端末の壁紙色に依存しないようにする
            AppTheme(dynamicColor = false) {
                TodoDetailScreen(
                    item =
                        TodoItem(
                            id = 1,
                            title = "牛乳を買う",
                            detail = "低脂肪のものを2本。\n帰りにスーパーに寄る。",
                            done = false,
                        ),
                    onBack = {},
                )
            }
        }
        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    fun todoDetail_loading() {
        composeTestRule.setContent {
            AppTheme(dynamicColor = false) {
                TodoDetailScreen(item = null, onBack = {})
            }
        }
        composeTestRule.onRoot().captureRoboImage()
    }
}
