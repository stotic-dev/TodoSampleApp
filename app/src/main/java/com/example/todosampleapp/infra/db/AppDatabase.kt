package com.example.todosampleapp.infra.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todosampleapp.infra.db.todo.TodoDao
import com.example.todosampleapp.infra.db.todo.TodoEntity

@Database(
    entities = [TodoEntity::class],
    version = 2,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}
