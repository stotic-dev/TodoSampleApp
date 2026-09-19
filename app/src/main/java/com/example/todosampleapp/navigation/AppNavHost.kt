package com.example.todosampleapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.todosampleapp.feature.todoadd.TodoAddRoute
import com.example.todosampleapp.feature.todoadd.todoAddScreen
import com.example.todosampleapp.feature.tododetail.TodoDetailRoute
import com.example.todosampleapp.feature.tododetail.todoDetailScreen
import com.example.todosampleapp.feature.todolist.TodoListRoute
import com.example.todosampleapp.feature.todolist.todoListScreen

/**
 * 画面間の遷移を配線する。
 * 各 feature は自分の Route しか知らないので、他画面への遷移はここでラムダとして渡す。
 */
@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = TodoListRoute,
    ) {
        todoListScreen(
            onNavigateToAdd = { navController.navigate(TodoAddRoute) },
            onNavigateToDetail = { id -> navController.navigate(TodoDetailRoute(id)) },
        )
        todoAddScreen(onBack = { navController.popBackStack() })
        todoDetailScreen(onBack = { navController.popBackStack() })
    }
}
