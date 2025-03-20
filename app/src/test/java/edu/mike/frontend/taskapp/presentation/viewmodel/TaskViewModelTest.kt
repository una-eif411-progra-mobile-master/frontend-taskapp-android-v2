package edu.mike.frontend.taskapp.presentation.viewmodel

import android.util.Log
import edu.mike.frontend.taskapp.domain.model.Priority
import edu.mike.frontend.taskapp.domain.model.Status
import edu.mike.frontend.taskapp.domain.model.Task
import edu.mike.frontend.taskapp.domain.repository.TaskRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import java.util.Date

/**
 * Unit tests for the TaskViewModel class.
 */
@ExperimentalCoroutinesApi
class TaskViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @MockK
    private lateinit var repository: TaskRepository

    private lateinit var viewModel: TaskViewModel

    private val mockPriority = Priority(1L, "Medium")
    private val mockStatus = Status(1L, "Todo")
    private val currentDate = Date()

    private val mockTask = Task(
        id = 1L,
        title = "Test Task",
        notes = "Test Notes",
        createdDate = currentDate,
        dueDate = currentDate,
        priority = mockPriority,
        status = mockStatus
    )

    private val mockTaskList = listOf(
        mockTask, Task(
            id = 2L,
            title = "Task 2",
            notes = "Notes 2",
            createdDate = currentDate,
            dueDate = currentDate,
            priority = Priority(2L, "Low"),
            status = Status(2L, "In Progress")
        )
    )

    /**
     * Sets up the test environment before each test.
     */
    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)

        // Mock Android Log
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0

        viewModel = TaskViewModel(repository)
    }

    /**
     * Resets the test environment after each test.
     */
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**
     * Tests the findAllTasks function to ensure it updates the taskList with a success result.
     */
    @Test
    fun `findAllTasks should update taskList with success result`() = runTest {
        // Arrange
        coEvery { repository.findAllTasks() } returns Result.success(mockTaskList)

        // Act
        viewModel.findAllTasks()
        advanceUntilIdle()

        // Assert
        assertEquals(mockTaskList, viewModel.taskList.value)
        coVerify { repository.findAllTasks() }
    }

    /**
     * Tests the findAllTasks function to ensure it keeps an empty list on failure.
     */
    @Test
    fun `findAllTasks should keep empty list on failure`() = runTest {
        // Arrange
        coEvery { repository.findAllTasks() } returns Result.failure(Exception("Test error"))

        // Act
        viewModel.findAllTasks()
        advanceUntilIdle()

        // Assert
        assertEquals(emptyList<Task>(), viewModel.taskList.value)
        coVerify { repository.findAllTasks() }
    }

    /**
     * Tests the selectTaskById function to ensure it updates the selectedTask with a success result.
     */
    @Test
    fun `selectTaskById should update selectedTask with success result`() = runTest {
        // Arrange
        coEvery { repository.findTaskById(1L) } returns Result.success(mockTask)

        // Act
        viewModel.selectTaskById(1L)
        advanceUntilIdle()

        // Assert
        assertEquals(mockTask, viewModel.selectedTask.value)
        coVerify { repository.findTaskById(1L) }
    }

    /**
     * Tests the selectTaskById function to ensure it keeps null on failure.
     */
    @Test
    fun `selectTaskById should keep null on failure`() = runTest {
        // Arrange
        coEvery { repository.findTaskById(1L) } returns Result.failure(Exception("Test error"))

        // Act
        viewModel.selectTaskById(1L)
        advanceUntilIdle()

        // Assert
        assertNull(viewModel.selectedTask.value)
        coVerify { repository.findTaskById(1L) }
    }

    /**
     * Tests the initial state of the ViewModel to ensure it is empty.
     */
    @Test
    fun `initial state should be empty`() {
        assertEquals(emptyList<Task>(), viewModel.taskList.value)
        assertNull(viewModel.selectedTask.value)
    }
}