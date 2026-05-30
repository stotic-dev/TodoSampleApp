package com.example.todosampleapp.feature.todoAdd

import com.example.todosampleapp.data.todo.TodoRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TodoAddViewModelTest {
    private val testDispatcher = UnconfinedTestDispatcher()

    @MockK
    lateinit var todoRepository: TodoRepository

    @InjectMockKs
    lateinit var viewModel: TodoAddViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onChangeTitle - inputItemのtitleが更新される`() {
        viewModel.onChangeTitle("新しいタイトル")
        assertEquals("新しいタイトル", viewModel.inputItem.value.title)
    }

    @Test
    fun `onChangeDetail - inputItemのdetailが更新される`() {
        viewModel.onChangeDetail("新しい詳細")
        assertEquals("新しい詳細", viewModel.inputItem.value.detail)
    }

    @Test
    fun `onClickAddButton - addTodoが入力値で呼び出され、 完了後にstateがCOMPLETEになる`() =
        runTest {
            coEvery { todoRepository.addTodo(any(), any()) } returns Unit
            viewModel.onChangeTitle("タイトル")
            viewModel.onChangeDetail("詳細")

            viewModel.onClickAddButton()

            coVerify { todoRepository.addTodo("タイトル", "詳細") }
            assertEquals(TodoAddUIState.COMPLETE, viewModel.state.value)
        }
}
