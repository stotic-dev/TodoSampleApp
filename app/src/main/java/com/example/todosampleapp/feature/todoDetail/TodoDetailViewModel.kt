package com.example.todosampleapp.feature.todoDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.todosampleapp.data.todo.TodoRepository
import com.example.todosampleapp.domain.todo.TodoItem
import com.example.todosampleapp.ui.navigation.NavRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoDetailViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val todoRepository: TodoRepository,
    ) : ViewModel() {
        private val route: NavRoute.TodoDetail = savedStateHandle.toRoute()

        val item: StateFlow<TodoItem?> =
            todoRepository
                .observeById(route.id)
                .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

        fun onChangeDoneStatus() =
            viewModelScope.launch {
                val item = item.value ?: return@launch
                todoRepository.toggleDone(item.id)
            }
    }
