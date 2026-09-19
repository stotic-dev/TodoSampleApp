package com.example.todosampleapp.feature.tododetail.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todosampleapp.domain.todo.TodoItem
import com.example.todosampleapp.domain.todo.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Navigation の Route 引数を [SavedStateHandle] から読み取るためのキー。
 * entry モジュールの Route data class のプロパティ名と一致させること。
 */
const val TODO_DETAIL_ID_ARG = "id"

@HiltViewModel
class TodoDetailViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val todoRepository: TodoRepository,
    ) : ViewModel() {
        private val todoId: Int = checkNotNull(savedStateHandle[TODO_DETAIL_ID_ARG])

        val item: StateFlow<TodoItem?> =
            todoRepository
                .observeById(todoId)
                .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

        fun onChangeDoneStatus() =
            viewModelScope.launch {
                val item = item.value ?: return@launch
                todoRepository.toggleDone(item.id)
            }

        fun onDeleteButtonClick() =
            viewModelScope.launch {
                val item = item.value ?: return@launch
                todoRepository.deleteItem(item.id)
            }
    }
