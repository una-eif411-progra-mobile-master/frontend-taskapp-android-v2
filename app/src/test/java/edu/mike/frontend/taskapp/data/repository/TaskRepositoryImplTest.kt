package edu.mike.frontend.taskapp.data.repository

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
import io.mockk.impl.annotations.MockK
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
        assertTrue(result.exceptionOrNull() is DomainError.NetworkError)
        coVerify { dataSource.getTasks() }
    }

    // ... rest of test methods with similar updates ...
}