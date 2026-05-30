package com.example.todosampleapp.feature.todoAdd

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todosampleapp.data.todo.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoAddViewModel
    @Inject
    constructor(
        private val todoRepository: TodoRepository,
    ) : ViewModel() {
        private var _state: MutableStateFlow<TodoAddUIState> = MutableStateFlow(TodoAddUIState.EDITING)
        val state = _state.asStateFlow()

        private var _inputItem = MutableStateFlow(AddTodoInputItem.initial)
        val inputItem = _inputItem.asStateFlow()

        fun onChangeTitle(title: String) {
            _inputItem.update { it.copy(title = title) }
        }

        fun onChangeDetail(detail: String) {
            _inputItem.update { it.copy(detail = detail) }
        }

        fun onClickAddButton() {
            viewModelScope.launch {
                todoRepository.addTodo(_inputItem.value.title, _inputItem.value.detail)
                _state.update { TodoAddUIState.COMPLETE }
            }
        }
    }
