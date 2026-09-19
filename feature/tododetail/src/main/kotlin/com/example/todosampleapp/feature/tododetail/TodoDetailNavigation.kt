package com.example.todosampleapp.feature.tododetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.todosampleapp.feature.tododetail.ui.TodoDetailScreen
import kotlinx.serialization.Serializable

/**
 * Todo 詳細画面の Route。
 * プロパティ名 `id` は TodoDetailViewModel の TODO_DETAIL_ID_ARG と一致させる必要がある。
 */
@Serializable
data class TodoDetailRoute(
    val id: Int,
)

/** Todo 詳細画面を NavGraph に登録する */
fun NavGraphBuilder.todoDetailScreen(onBack: () -> Unit) {
    composable<TodoDetailRoute> {
        TodoDetailEntry(onBack = onBack)
    }
}

@Composable
internal fun TodoDetailEntry(
    onBack: () -> Unit,
    viewModel: TodoDetailViewModel = hiltViewModel(),
) {
    val item by viewModel.item.collectAsStateWithLifecycle()
    TodoDetailScreen(
        item = item,
        onBack = onBack,
        onChangeDoneStatus = { viewModel.onChangeDoneStatus() },
        onClickDeleteButton = {
            viewModel.onDeleteButtonClick()
            onBack()
        },
    )
}
