package com.example.todosampleapp.feature.todoList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todosampleapp.data.todo.TodoRepository
import com.example.todosampleapp.domain.todo.TodoItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoListViewModel @Inject constructor(
    private val repository: TodoRepository,
) : ViewModel() {

    val todos: StateFlow<List<TodoItem>> = repository.observeTodos()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun addTodo(title: String, detail: String = "") {
        viewModelScope.launch { repository.addTodo(title, detail) }
    }

    fun toggleDone(id: Int) {
        viewModelScope.launch { repository.toggleDone(id) }
    }

    fun clearAllTodos() {
        viewModelScope.launch { repository.clearAll() }
    }
}
