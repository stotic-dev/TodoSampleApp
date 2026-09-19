package com.example.todosampleapp.feature.todolist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.todosampleapp.feature.todolist.ui.TodoListScreen
import kotlinx.serialization.Serializable

/** Todo 一覧画面の Route */
@Serializable
data object TodoListRoute

/**
 * Todo 一覧画面を NavGraph に登録する。
 * 他画面への遷移は app 側から渡されるラムダで行い、他 feature には依存しない。
 */
fun NavGraphBuilder.todoListScreen(
    onNavigateToAdd: () -> Unit,
    onNavigateToDetail: (todoId: Int) -> Unit,
) {
    composable<TodoListRoute> {
        TodoListEntry(
            onNavigateToAdd = onNavigateToAdd,
            onNavigateToDetail = onNavigateToDetail,
        )
    }
}

@Composable
internal fun TodoListEntry(
    onNavigateToAdd: () -> Unit,
    onNavigateToDetail: (todoId: Int) -> Unit,
    viewModel: TodoListViewModel = hiltViewModel(),
) {
    val todos by viewModel.todos.collectAsStateWithLifecycle()
    TodoListScreen(
        todos = todos,
        onAddTodo = onNavigateToAdd,
        onTodoClick = { onNavigateToDetail(it.id) },
        onToggleDone = viewModel::toggleDone,
        onClearAllTodo = viewModel::clearAllTodos,
    )
}
