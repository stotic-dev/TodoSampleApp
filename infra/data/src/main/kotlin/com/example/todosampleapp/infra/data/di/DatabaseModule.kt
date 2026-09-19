package com.example.todosampleapp.infra.data.di

import android.content.Context
import androidx.room.Room
import com.example.todosampleapp.infra.data.db.AppDatabase
import com.example.todosampleapp.infra.data.db.todo.TodoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
    ): AppDatabase =
        Room
            .databaseBuilder(context, AppDatabase::class.java, "todo-database")
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()

    @Provides
    fun provideTodoDao(database: AppDatabase): TodoDao = database.todoDao()
}
