package edu.mike.frontend.taskapp.data.repository

import android.util.Log
import edu.mike.frontend.taskapp.data.datasource.TaskDataSource
import edu.mike.frontend.taskapp.data.datasource.model.PriorityDto
import edu.mike.frontend.taskapp.data.datasource.model.StatusDto
import edu.mike.frontend.taskapp.data.datasource.model.TaskDto
import edu.mike.frontend.taskapp.data.mapper.TaskMapper
import edu.mike.frontend.taskapp.domain.error.DomainError
import edu.mike.frontend.taskapp.domain.model.Priority
import edu.mike.frontend.taskapp.domain.model.Status
import edu.mike.frontend.taskapp.domain.model.Task
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.util.Date

/**
 * Unit tests for the TaskRepositoryImpl class.
 */
class TaskRepositoryImplTest {
    @MockK
    private lateinit var dataSource: TaskDataSource

    @MockK
    private lateinit var taskMapper: TaskMapper

    private lateinit var repository: TaskRepositoryImpl

    private val mockPriorityDto = PriorityDto(1L, "High")
    private val mockStatusDto = StatusDto(1L, "Todo")
    private val mockPriority = Priority(1L, "High")
    private val mockStatus = Status(1L, "Todo")
    private val currentDate = Date()

    private val mockTaskDto = TaskDto(
        id = 1L,
        title = "Test Task",
        notes = "Test Notes",
        createdDate = currentDate,
        dueDate = currentDate,
        priority = mockPriorityDto,
        status = mockStatusDto
    )

    private val mockTask = Task(
        id = 1L,
        title = "Test Task",
        notes = "Test Notes",
        createdDate = currentDate,
        dueDate = currentDate,
        priority = mockPriority,
        status = mockStatus
    )

    /**
     * Sets up the test environment before each test.
     */
    @Before
    fun setup() {
        MockKAnnotations.init(this)

        // Mock Android Log class
        mockkStatic(Log::class)
        every { Log.e(any(), any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0

        repository = TaskRepositoryImpl(dataSource, taskMapper)
    }

    /**
     * Tests the findAllTasks function to ensure it returns tasks when the data source returns successfully.
     */
    @Test
    fun `findAllTasks should return tasks when data source returns successfully`() = runTest {
        // Arrange
        coEvery { dataSource.getTasks() } returns flowOf(listOf(mockTaskDto))
        coEvery { taskMapper.mapToDomain(any()) } returns mockTask

        // Act
        val result = repository.findAllTasks()

        // Assert
        assertTrue(result.isSuccess)
        assertEquals(listOf(mockTask), result.getOrNull())
        coVerify { dataSource.getTasks() }
        coVerify { taskMapper.mapToDomain(mockTaskDto) }
    }

    /**
     * Tests the findAllTasks function to ensure it returns a network error when an IOException occurs.
     */
    @Test
    fun `findAllTasks should return network error when IOException occurs`() = runTest {
        // Arrange
        coEvery { dataSource.getTasks() } throws IOException("Network error")

        // Act
        val result = repository.findAllTasks()

        // Assert
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(
            "Expected NetworkError but got ${exception?.javaClass?.simpleName}",
            exception is DomainError.NetworkError
        )
        coVerify { dataSource.getTasks() }
    }

    /**
     * Tests the findAllTasks function to ensure it returns a mapping error when an IllegalArgumentException occurs.
     */
    @Test
    fun `findAllTasks should return mapping error when mapping fails`() = runTest {
        // Arrange
        coEvery { dataSource.getTasks() } returns flowOf(listOf(mockTaskDto))
        coEvery { taskMapper.mapToDomain(any()) } throws IllegalArgumentException("Mapping error")

        // Act
        val result = repository.findAllTasks()

        // Assert
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(
            "Expected MappingError but got ${exception?.javaClass?.simpleName}",
            exception is DomainError.MappingError
        )
        coVerify { dataSource.getTasks() }
    }

    /**
     * Tests the findTaskById function to ensure it returns a task when the data source returns successfully.
     */
    @Test
    fun `findTaskById should return task when data source returns successfully`() = runTest {
        // Arrange
        val taskId = 1L
        coEvery { dataSource.getTaskById(taskId) } returns mockTaskDto
        coEvery { taskMapper.mapToDomain(any()) } returns mockTask

        // Act
        val result = repository.findTaskById(taskId)

        // Assert
        assertTrue(result.isSuccess)
        assertEquals(mockTask, result.getOrNull())
        coVerify { dataSource.getTaskById(taskId) }
        coVerify { taskMapper.mapToDomain(mockTaskDto) }
    }

    /**
     * Tests the findTaskById function to ensure it returns a task error when the task is not found.
     */
    @Test
    fun `findTaskById should return task error when task not found`() = runTest {
        // Arrange
        val taskId = 1L
        coEvery { dataSource.getTaskById(taskId) } returns null

        // Act
        val result = repository.findTaskById(taskId)

        // Assert
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(
            "Expected TaskError but got ${exception?.javaClass?.simpleName}",
            exception is DomainError.TaskError
        )
        coVerify { dataSource.getTaskById(taskId) }
    }

    /**
     * Tests the findTaskById function to ensure it returns a network error when an IOException occurs.
     */
    @Test
    fun `findTaskById should return network error when IOException occurs`() = runTest {
        // Arrange
        val taskId = 1L
        coEvery { dataSource.getTaskById(taskId) } throws IOException("Network error")

        // Act
        val result = repository.findTaskById(taskId)

        // Assert
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(
            "Expected NetworkError but got ${exception?.javaClass?.simpleName}",
            exception is DomainError.NetworkError
        )
        coVerify { dataSource.getTaskById(taskId) }
    }

    /**
     * Tests the findTaskById function to ensure it returns a mapping error when an IllegalArgumentException occurs.
     */
    @Test
    fun `findTaskById should return mapping error when mapping fails`() = runTest {
        // Arrange
        val taskId = 1L
        coEvery { dataSource.getTaskById(taskId) } returns mockTaskDto
        coEvery { taskMapper.mapToDomain(any()) } throws IllegalArgumentException("Mapping error")

        // Act
        val result = repository.findTaskById(taskId)

        // Assert
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(
            "Expected MappingError but got ${exception?.javaClass?.simpleName}",
            exception is DomainError.MappingError
        )
        coVerify { dataSource.getTaskById(taskId) }
    }

    /**
     * Tests the findTaskById function to ensure it returns an unknown error when an unexpected exception occurs.
     */
    @Test
    fun `findTaskById should return unknown error when unexpected exception occurs`() = runTest {
        // Arrange
        val taskId = 1L
        coEvery { dataSource.getTaskById(taskId) } throws RuntimeException("Unexpected error")

        // Act
        val result = repository.findTaskById(taskId)

        // Assert
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(
            "Expected UnknownError but got ${exception?.javaClass?.simpleName}",
            exception is DomainError.UnknownError
        )
        coVerify { dataSource.getTaskById(taskId) }
    }
}