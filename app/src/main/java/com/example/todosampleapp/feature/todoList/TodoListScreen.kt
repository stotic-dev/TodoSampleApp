package com.example.todosampleapp.feature.todoList

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.todosampleapp.domain.todo.TodoItem
import com.example.todosampleapp.ui.theme.TodoSampleAppTheme

@Composable
fun TodoListScreen(
    onAddTodoClick: () -> Unit,
    viewModel: TodoListViewModel = hiltViewModel(),
) {
    val todos by viewModel.todos.collectAsStateWithLifecycle()
    TodoListContent(
        todos = todos,
        onAddTodo = onAddTodoClick,
        onToggleDone = { viewModel.toggleDone(it) },
        onClearAllTodo = { viewModel.clearAllTodos() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TodoListContent(
    todos: List<TodoItem>,
    onAddTodo: () -> Unit,
    onToggleDone: (Int) -> Unit,
    onClearAllTodo: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Todo") },
                actions = {
                    IconButton(onClearAllTodo) {
                        Icon(imageVector = Icons.Default.Clear, contentDescription = "削除")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddTodo) {
                Icon(Icons.Default.Add, contentDescription = "追加")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            items(todos, key = { it.id }) { todo ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = todo.done,
                        onCheckedChange = { onToggleDone(todo.id) }
                    )
                    Text(
                        text = todo.title,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TodoListScreenPreview() {
    TodoSampleAppTheme {
        TodoListContent(
            todos = listOf(
                TodoItem(1, "牛乳を買う", false),
                TodoItem(2, "洗濯する", false),
                TodoItem(3, "運動する", true),
            ),
            onAddTodo = {},
            onToggleDone = {},
            onClearAllTodo = {}
        )
    }
}
