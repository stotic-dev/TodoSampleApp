package com.example.todosampleapp.domain.todo

import org.junit.Assert.assertEquals
import org.junit.Test

class TodoListTest {

    // region add

    @Test
    fun `add - 空リストに追加する`() {
        val result = TodoList().add("牛乳を買う")
        assertEquals(TodoList(listOf(TodoItem(1, "牛乳を買う", false))), result)
    }

    @Test
    fun `add - 複数追加する`() {
        val result = TodoList().add("タスク1").add("タスク2").add("タスク3")
        assertEquals(
            TodoList(listOf(
                TodoItem(1, "タスク1", false),
                TodoItem(2, "タスク2", false),
                TodoItem(3, "タスク3", false),
            )),
            result
        )
    }

    // endregion

    // region toggleDone

    @Test
    fun `toggleDone - falseのアイテムをトグルするとtrueになる`() {
        val result = TodoList(listOf(TodoItem(1, "牛乳を買う", false))).toggleDone(1)
        assertEquals(TodoList(listOf(TodoItem(1, "牛乳を買う", true))), result)
    }

    @Test
    fun `toggleDone - trueのアイテムをトグルするとfalseになる`() {
        val result = TodoList(listOf(TodoItem(1, "牛乳を買う", true))).toggleDone(1)
        assertEquals(TodoList(listOf(TodoItem(1, "牛乳を買う", false))), result)
    }

    @Test
    fun `toggleDone - 指定したid以外のアイテムは変更されない`() {
        val todoList = TodoList(listOf(TodoItem(1, "タスク1", false), TodoItem(2, "タスク2", false)))
        val result = todoList.toggleDone(1)
        assertEquals(
            TodoList(listOf(TodoItem(1, "タスク1", true), TodoItem(2, "タスク2", false))),
            result
        )
    }

    @Test
    fun `toggleDone - 存在しないidを指定してもリストは変化しない`() {
        val todoList = TodoList(listOf(TodoItem(1, "タスク1", false)))
        assertEquals(todoList, todoList.toggleDone(99))
    }
}
