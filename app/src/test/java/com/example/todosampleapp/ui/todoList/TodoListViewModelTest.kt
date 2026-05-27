package com.example.todosampleapp.ui.todoList

import com.example.todosampleapp.data.todo.TodoRepository
import com.example.todosampleapp.domain.todo.TodoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TodoListViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var fakeRepository: FakeTodoRepository
    private lateinit var viewModel: TodoListViewModel

    @Before
    fun setUp() = runTest(testDispatcher) {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeTodoRepository().apply {
            seed(
                listOf(
                    TodoItem(1, "牛乳を買う", false),
                    TodoItem(2, "洗濯する", false),
                    TodoItem(3, "運動する", true),
                )
            )
        }
        viewModel = TodoListViewModel(fakeRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `addTodo - todosに1件追加される`() {
        val before = viewModel.todos.value.size
        viewModel.addTodo("新しいタスク")
        assertEquals(before + 1, viewModel.todos.value.size)
    }

    @Test
    fun `addTodo - 追加したtitleがtodosに反映される`() {
        viewModel.addTodo("新しいタスク")
        assertEquals("新しいタスク", viewModel.todos.value.last().title)
    }

    @Test
    fun `toggleDone - 指定したidのdoneが反転する`() {
        val target = viewModel.todos.value.first()
        viewModel.toggleDone(target.id)
        val result = viewModel.todos.value.first { it.id == target.id }
        assertEquals(!target.done, result.done)
    }

    @Test
    fun `toggleDone - 指定したid以外のアイテムは変化しない`() {
        val before = viewModel.todos.value
        val targetId = before.first().id
        viewModel.toggleDone(targetId)
        val after = viewModel.todos.value
        val unchanged = before.filter { it.id != targetId }
        assertEquals(unchanged, after.filter { it.id != targetId })
    }
}

private class FakeTodoRepository : TodoRepository {
    private val state = MutableStateFlow<List<TodoItem>>(emptyList())

    fun seed(items: List<TodoItem>) {
        state.value = items
    }

    override fun observeTodos(): Flow<List<TodoItem>> = state.asStateFlow()

    override suspend fun addTodo(title: String, detail: String) {
        val newId = (state.value.maxOfOrNull { it.id } ?: 0) + 1
        state.update { it + TodoItem(newId, title, false, detail) }
    }

    override suspend fun toggleDone(id: Int) {
        state.update { list ->
            list.map { if (it.id == id) it.copy(done = !it.done) else it }
        }
    }

    override suspend fun clearAll() {
        state.value = emptyList()
    }
}
