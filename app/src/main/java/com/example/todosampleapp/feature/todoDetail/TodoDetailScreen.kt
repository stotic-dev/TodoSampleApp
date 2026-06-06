package com.example.todosampleapp.feature.todoDetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        onChangeDoneStatus = { viewModel.onChangeDoneStatus() },
        onClickDeleteButton = {
            viewModel.onDeleteButtonClick()
            onBack()
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TodoDetailScreen(
    item: TodoItem?,
    onBack: () -> Unit,
    onChangeDoneStatus: (Boolean) -> Unit = {},
    onClickDeleteButton: () -> Unit = {},
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
                actions = {
                    IconButton(onClick = onClickDeleteButton) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "削除",
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
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                ) {
                    TodoItemColumn("Title", item.title)
                    TodoItemColumn("Description", item.detail)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            "Done",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Switch(
                            checked = item.done,
                            onCheckedChange = onChangeDoneStatus,
                            enabled = true,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TodoItemColumn(
    title: String,
    value: String,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
        )
        Text(
            value,
            fontSize = 18.sp,
        )
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
