package com.example.todosampleapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.todosampleapp.feature.todoAdd.TodoAddRoute
import com.example.todosampleapp.feature.todoList.TodoListScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NavRoute.TodoList,
    ) {
        composable<NavRoute.TodoList> {
            TodoListScreen(
                onAddTodoClick = { navController.navigate(NavRoute.TodoAdd) },
            )
        }
        composable<NavRoute.TodoAdd> {
            TodoAddRoute(navController = navController)
        }
    }
}
