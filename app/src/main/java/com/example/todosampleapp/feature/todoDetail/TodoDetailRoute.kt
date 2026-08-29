package com.example.todosampleapp.feature.todoDetail

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun TodoDetailRoute(navController: NavHostController) {
    TodoDetailScreen(
        onBack = { navController.popBackStack() },
    )
}
