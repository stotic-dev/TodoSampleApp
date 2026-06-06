package com.example.todosampleapp.feature.todoList

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.todosampleapp.ui.navigation.NavRoute

@Composable
fun TodoListRoute(navController: NavHostController) {
    TodoListScreen(
        onAddTodoClick = { navController.navigate(NavRoute.TodoAdd) },
        onTodoClick = { todo -> navController.navigate(NavRoute.TodoDetail(todo.id)) },
    )
}
