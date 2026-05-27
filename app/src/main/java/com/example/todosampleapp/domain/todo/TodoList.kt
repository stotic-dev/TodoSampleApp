package com.example.todosampleapp.domain.todo

data class TodoList(val items: List<TodoItem> = emptyList()) {

    fun add(title: String, detail: String = ""): TodoList {
        val newId = (items.maxOfOrNull { it.id } ?: 0) + 1
        return copy(items = items + TodoItem(newId, title, false, detail))
    }

    fun toggleDone(id: Int): TodoList {
        return copy(items = items.map { if (it.id == id) it.copy(done = !it.done) else it })
    }
}
