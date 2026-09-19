package com.example.todosampleapp.infra.data.db.todo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todos")
data class TodoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val done: Boolean,
    val detail: String = "",
)
