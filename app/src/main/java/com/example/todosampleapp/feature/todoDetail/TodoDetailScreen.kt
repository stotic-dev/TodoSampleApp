package com.example.todosampleapp.feature.todoDetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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

@Composable
fun TodoDetailScreen(
    viewModel: TodoDetailViewModel = hiltViewModel(),
    onBack: () -> Unit,
) {
    val item by viewModel.item.collectAsStateWithLifecycle()
    TodoDetailScreen(
        item = item,
        onBack = onBack,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TodoDetailScreen(
    item: TodoItem?,
    onBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "戻る",
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Box(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            if (item == null) {
                CircularProgressIndicator()
            } else {
                Column(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                ) {
                    Text(item.title)
                    Text(item.detail, modifier = Modifier.padding(top = 8.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TodoDetailScreenPreview() {
    TodoDetailScreen(
        onBack = {},
        item =
            TodoItem(
                id = 1,
                title = "hoge",
                detail = "hogehoge",
                done = false,
            ),
    )
}
