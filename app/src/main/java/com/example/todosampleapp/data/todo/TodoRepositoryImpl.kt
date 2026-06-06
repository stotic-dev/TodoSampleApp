package com.example.todosampleapp.data.todo

import com.example.todosampleapp.domain.todo.TodoItem
import com.example.todosampleapp.infra.db.todo.TodoDao
import com.example.todosampleapp.infra.db.todo.TodoEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TodoRepositoryImpl
    @Inject
    constructor(
        private val dao: TodoDao,
    ) : TodoRepository {
        override fun observeTodos(): Flow<List<TodoItem>> =
            dao.observeAll().map { entities -> entities.map { it.toDomain() } }

        override fun observeById(id: Int): Flow<TodoItem?> = dao.observeById(id).map { it?.toDomain() }

        override suspend fun addTodo(
            title: String,
            detail: String,
        ) {
            dao.insert(TodoEntity(title = title, done = false, detail = detail))
        }

        override suspend fun toggleDone(id: Int) {
            val current = dao.findById(id) ?: return
            dao.update(current.copy(done = !current.done))
        }

        override suspend fun deleteItem(id: Int) {
            dao.deleteById(id)
        }

        override suspend fun clearAll() {
            dao.nukeTable()
        }

        private fun TodoEntity.toDomain(): TodoItem = TodoItem(id = id, title = title, done = done, detail = detail)
    }
