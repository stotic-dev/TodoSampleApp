package com.example.todosampleapp.data.todo

import com.example.todosampleapp.domain.todo.TodoItem
import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    fun observeTodos(): Flow<List<TodoItem>>
    suspend fun addTodo(title: String, detail: String = "")
    suspend fun toggleDone(id: Int)

    suspend fun clearAll()
}
