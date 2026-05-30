package com.example.todosampleapp.feature.todoAdd

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

@Composable
fun TodoAddRoute(
    navController: NavHostController,
    viewModel: TodoAddViewModel = hiltViewModel(),
) {
    TodoAddScreen(
        onSaved = {
            navController.popBackStack()
        },
        onCancel = {
            navController.popBackStack()
        },
    )
}
