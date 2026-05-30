package com.example.todosampleapp.ui.todoList

import com.example.todosampleapp.data.todo.TodoRepository
import com.example.todosampleapp.domain.todo.TodoItem
import com.example.todosampleapp.feature.todoList.TodoListViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TodoListViewModelTest {
    private val testDispatcher = UnconfinedTestDispatcher()

    @MockK
    lateinit var todoRepository: TodoRepository

    private lateinit var viewModel: TodoListViewModel

    // mock の observeTodos が返す実体。addTodo/toggleDone の coAnswers でこの値を更新し、
    // 本物の Repository と同じように todos へ反映されることを再現する。
    private val todosFlow =
        MutableStateFlow(
            listOf(
                TodoItem(1, "牛乳を買う", false),
                TodoItem(2, "洗濯する", false),
                TodoItem(3, "運動する", true),
            ),
        )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this)

        every { todoRepository.observeTodos() } returns todosFlow
        coEvery { todoRepository.addTodo(any(), any()) } coAnswers {
            val title = firstArg<String>()
            val newId = (todosFlow.value.maxOfOrNull { it.id } ?: 0) + 1
            todosFlow.update { it + TodoItem(newId, title, false) }
        }
        coEvery { todoRepository.toggleDone(any()) } coAnswers {
            val id = firstArg<Int>()
            todosFlow.update { list ->
                list.map { if (it.id == id) it.copy(done = !it.done) else it }
            }
        }

        // VM は構築時に observeTodos() を購読するため、スタブ設定後に生成する。
        viewModel = TodoListViewModel(todoRepository)
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
        coVerify { todoRepository.addTodo("新しいタスク", any()) }
    }

    @Test
    fun `addTodo - 追加したtitleがtodosに反映される`() {
        viewModel.addTodo("新しいタスク")
        assertEquals(
            "新しいタスク",
            viewModel.todos.value
                .last()
                .title,
        )
    }

    @Test
    fun `toggleDone - 指定したidのdoneが反転する`() {
        val target = viewModel.todos.value.first()
        viewModel.toggleDone(target.id)
        val result = viewModel.todos.value.first { it.id == target.id }
        assertEquals(!target.done, result.done)
        coVerify { todoRepository.toggleDone(target.id) }
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
