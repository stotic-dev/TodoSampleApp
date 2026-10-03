package com.example.todosampleapp.feature.todoadd

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.todosampleapp.feature.todoadd.ui.TodoAddScreen
import kotlinx.serialization.Serializable

/** Todo 追加画面の Route */
@Serializable
data object TodoAddRoute

/** Todo 追加画面を NavGraph に登録する */
fun NavGraphBuilder.todoAddScreen(onBack: () -> Unit) {
    composable<TodoAddRoute> {
        TodoAddEntry(onBack = onBack)
    }
}

@Composable
internal fun TodoAddEntry(
    onBack: () -> Unit,
    viewModel: TodoAddViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(state) {
        if (state == TodoAddUIState.COMPLETE) {
            onBack()
        }
    }

    val inputItem by viewModel.inputItem.collectAsStateWithLifecycle()
    TodoAddScreen(
        title = inputItem.title,
        detail = inputItem.detail,
        canSave = inputItem.canSave,
        onClickSaveButton = viewModel::onClickAddButton,
        onCancel = onBack,
        onValueChangeTitle = viewModel::onChangeTitle,
        onValueChangeDetail = viewModel::onChangeDetail,
    )
}
