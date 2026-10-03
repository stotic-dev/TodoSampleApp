package com.example.todosampleapp.feature.todolist.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todosampleapp.designsystem.theme.AppTheme
import com.example.todosampleapp.domain.todo.TodoItem

/**
 * Todo 一覧画面（ステートレス）。
 * 状態とイベントハンドラはすべて引数で受け取り、ViewModel には依存しない。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListScreen(
    todos: List<TodoItem>,
    onAddTodo: () -> Unit,
    onTodoClick: (TodoItem) -> Unit,
    onToggleDone: (Int) -> Unit,
    onClearAllTodo: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Todo") },
                actions = {
                    IconButton(onClearAllTodo) {
                        Icon(imageVector = Icons.Default.Clear, contentDescription = "削除")
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddTodo) {
                Icon(Icons.Default.Add, contentDescription = "追加")
            }
        },
    ) { innerPadding ->
        LazyColumn(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
        ) {
            items(todos, key = { it.id }) { todo ->
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .clickable { onTodoClick(todo) }
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Checkbox(
                        checked = todo.done,
                        onCheckedChange = { onToggleDone(todo.id) },
                    )
                    Text(
                        text = todo.title,
                        modifier = Modifier.padding(start = 8.dp),
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "詳細",
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TodoListScreenPreview() {
    AppTheme {
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
