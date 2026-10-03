package com.example.todosampleapp.feature.todoadd.ui

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todosampleapp.designsystem.theme.AppTheme

/**
 * Todo 追加画面（ステートレス）。
 * 入力値と保存可否はプリミティブで受け取り、ViewModel や UiState の型には依存しない。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoAddScreen(
    title: String,
    detail: String,
    canSave: Boolean,
    onClickSaveButton: () -> Unit,
    onCancel: () -> Unit,
    onValueChangeTitle: (title: String) -> Unit,
    onValueChangeDetail: (detail: String) -> Unit,
) {
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
            title = title,
            detail = detail,
            canSave = canSave,
            innerPadding = innerPadding,
            onClickSaveButton = onClickSaveButton,
            onCancel = onCancel,
            onValueChangeTitle = onValueChangeTitle,
            onValueChangeDetail = onValueChangeDetail,
        )
    }
}

@Composable
private fun TodoAddInputContent(
    title: String,
    detail: String,
    canSave: Boolean,
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
            value = title,
            onValueChange = onValueChangeTitle,
            label = { Text("タイトル") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = detail,
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
            enabled = canSave,
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
        TodoAddScreen(
            title = "牛乳を買う",
            detail = "低脂肪のもの",
            canSave = true,
            onClickSaveButton = {},
            onCancel = {},
            onValueChangeTitle = {},
            onValueChangeDetail = {},
        )
    }
}
