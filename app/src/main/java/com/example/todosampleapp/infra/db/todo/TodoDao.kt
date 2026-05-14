package com.example.todosampleapp.infra.db.todo

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.DeleteTable
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    @Query("SELECT * FROM todos ORDER BY id ASC")
    fun observeAll(): Flow<List<TodoEntity>>

    @Query("SELECT * FROM todos WHERE id = :id")
    suspend fun findById(id: Int): TodoEntity?

    @Insert
    suspend fun insert(entity: TodoEntity): Long

    @Update
    suspend fun update(entity: TodoEntity)

    @Query("DELETE FROM todos")
    suspend fun nukeTable();
}
