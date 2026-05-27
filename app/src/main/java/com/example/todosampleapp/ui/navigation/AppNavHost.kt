package com.example.todosampleapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.todosampleapp.feature.todoAdd.TodoAddScreen
import com.example.todosampleapp.feature.todoList.TodoListScreen
import com.example.todosampleapp.feature.todoList.TodoListViewModel

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
            val viewModel = hiltViewModel<TodoListViewModel>()
            TodoAddScreen(
                onSave = { title, detail ->
                    viewModel.addTodo(title, detail)
                    navController.popBackStack()
                },
                onCancel = { navController.popBackStack() },
            )
        }
    }
}
