package com.example.todosampleapp.feature.todolist.ui

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
 * TodoListScreen のスナップショットテスト。
 *
 * - 記録: ./gradlew :feature:TODOLISTi:recordRoborazziDebug
 * - 検証: ./gradlew :feature:TODOLISTi:verifyRoborazziDebug
 * - 差分: ./gradlew :feature:TODOLISTi:compareRoborazziDebug
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel7)
class TodoListScreenshotTest {
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
    fun todoList_withItems() {
        composeTestRule.setContent {
            // dynamicColor を無効化して端末の壁紙色に依存しないようにする
            AppTheme(dynamicColor = false) {
                TodoListScreen(
                    todos =
                        listOf(
                            TodoItem(1, "牛乳を買う", false),
                            TodoItem(2, "洗濯する", false),
                            TodoItem(3, "運動する", true),
                        ),
                    onAddTodo = {},
                    onTodoClick = {},
                    onToggleDone = {},
                    onClearAllTodo = {},
                )
            }
        }
        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    fun todoList_empty() {
        composeTestRule.setContent {
            AppTheme(dynamicColor = false) {
                TodoListScreen(
                    todos = emptyList(),
                    onAddTodo = {},
                    onTodoClick = {},
                    onToggleDone = {},
                    onClearAllTodo = {},
                )
            }
        }
        composeTestRule.onRoot().captureRoboImage()
    }
}
