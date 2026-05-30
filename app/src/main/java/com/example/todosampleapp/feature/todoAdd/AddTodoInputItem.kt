package com.example.todosampleapp.feature.todoAdd

data class AddTodoInputItem(
    val title: String,
    val detail: String,
) {
    val canSave: Boolean get() = !title.isEmpty() && !detail.isEmpty()

    companion object {
        val initial = AddTodoInputItem("", "")
    }
}
