package com.example.todosampleapp.feature.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.todosampleapp.ui.theme.TodoSampleAppTheme
import com.example.todosampleapp.feature.todoAdd.TodoAddScreen
import com.example.todosampleapp.feature.todoList.TodoListScreen
import com.example.todosampleapp.feature.todoList.TodoListViewModel
import com.example.todosampleapp.ui.navigation.AppNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoSampleAppTheme {
                val navController = rememberNavController()
                AppNavHost(navController)
            }
        }
    }
}
