package com.example.todosampleapp.feature.todoAdd

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.todosampleapp.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoAddScreen(
    viewModel: TodoAddViewModel = hiltViewModel(),
    onSaved: () -> Unit,
    onCancel: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(state) {
        if (state == TodoAddUIState.COMPLETE) {
            onSaved()
        }
    }

    val inputItem by viewModel.inputItem.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Todo追加") },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "戻る",
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        TodoAddInputContent(
            inputItem = inputItem,
            innerPadding = innerPadding,
            onClickSaveButton = {
                viewModel.onClickAddButton()
            },
            onCancel = onCancel,
            onValueChangeTitle = { viewModel.onChangeTitle(title = it) },
            onValueChangeDetail = { viewModel.onChangeDetail(detail = it) },
        )
    }
}

@Composable
fun TodoAddInputContent(
    inputItem: AddTodoInputItem,
    innerPadding: PaddingValues,
    onClickSaveButton: () -> Unit,
    onCancel: () -> Unit,
    onValueChangeTitle: (title: String) -> Unit,
    onValueChangeDetail: (detail: String) -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        OutlinedTextField(
            value = inputItem.title,
            onValueChange = onValueChangeTitle,
            label = { Text("タイトル") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = inputItem.detail,
            onValueChange = onValueChangeDetail,
            label = { Text("詳細") },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(160.dp),
        )
        Spacer(modifier = Modifier.size(8.dp))
        Button(
            onClick = onClickSaveButton,
            enabled = inputItem.canSave,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("保存")
        }
        OutlinedButton(
            onClick = onCancel,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("キャンセル")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TodoAddScreenPreview() {
    AppTheme {
        TodoAddScreen(onSaved = {}, onCancel = {})
    }
}
