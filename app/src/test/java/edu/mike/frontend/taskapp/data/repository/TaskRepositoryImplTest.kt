package edu.mike.frontend.taskapp.data.repository

    import edu.mike.frontend.taskapp.data.mapper.TaskMapper
    import edu.mike.frontend.taskapp.data.remote.dto.PriorityDto
    import edu.mike.frontend.taskapp.data.remote.dto.StatusDto
    import edu.mike.frontend.taskapp.data.remote.dto.TaskDto
    import edu.mike.frontend.taskapp.data.remote.TaskRemoteDataSource
    import edu.mike.frontend.taskapp.domain.model.Priority
    import edu.mike.frontend.taskapp.domain.model.Status
    import edu.mike.frontend.taskapp.domain.model.Task
    import io.mockk.MockKAnnotations
    import io.mockk.coEvery
    import io.mockk.coVerify
    import io.mockk.impl.annotations.MockK
    import kotlinx.coroutines.test.runTest
    import org.junit.Assert.assertEquals
    import org.junit.Assert.assertTrue
    import org.junit.Before
    import org.junit.Test
    import java.io.IOException
    import java.util.Date

    class TaskRepositoryImplTest {
        @MockK
        private lateinit var remoteDataSource: TaskRemoteDataSource

        @MockK
        private lateinit var taskMapper: TaskMapper

        private lateinit var repository: TaskRepositoryImpl

        private val mockPriorityDto = PriorityDto(1, "High")
        private val mockStatusDto = StatusDto(1, "Todo")
        private val mockPriority = Priority(1, "High")
        private val mockStatus = Status(1, "Todo")
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

        @Before
        fun setup() {
            MockKAnnotations.init(this)
            repository = TaskRepositoryImpl(remoteDataSource, taskMapper)
        }

        @Test
        fun `findAllTasks should return tasks when data source returns successfully`() = runTest {
            // Arrange
            val taskDtoList = listOf(mockTaskDto)
            val taskList = listOf(mockTask)
            coEvery { remoteDataSource.getAllTasks() } returns Result.success(taskDtoList)
            coEvery { taskMapper.mapToDomainList(taskDtoList) } returns taskList

            // Act
            val result = repository.findAllTasks()

            // Assert
            assertTrue(result.isSuccess)
            assertEquals(taskList, result.getOrNull())
            coVerify { remoteDataSource.getAllTasks() }
            coVerify { taskMapper.mapToDomainList(taskDtoList) }
        }

        @Test
        fun `findAllTasks should return failure when data source returns failure`() = runTest {
            // Arrange
            val exception = IOException("Network error")
            coEvery { remoteDataSource.getAllTasks() } returns Result.failure(exception)

            // Act
            val result = repository.findAllTasks()

            // Assert
            assertTrue(result.isFailure)
            assertEquals(exception, result.exceptionOrNull())
            coVerify { remoteDataSource.getAllTasks() }
        }

        @Test
        fun `findTaskById should return task when data source returns successfully`() = runTest {
            // Arrange
            val taskId = 1L
            coEvery { remoteDataSource.getTaskById(taskId) } returns Result.success(mockTaskDto)
            coEvery { taskMapper.mapToDomain(mockTaskDto) } returns mockTask

            // Act
            val result = repository.findTaskById(taskId)

            // Assert
            assertTrue(result.isSuccess)
            assertEquals(mockTask, result.getOrNull())
            coVerify { remoteDataSource.getTaskById(taskId) }
            coVerify { taskMapper.mapToDomain(mockTaskDto) }
        }

        @Test
        fun `findTaskById should return failure when data source returns failure`() = runTest {
            // Arrange
            val taskId = 1L
            val exception = IOException("Network error")
            coEvery { remoteDataSource.getTaskById(taskId) } returns Result.failure(exception)

            // Act
            val result = repository.findTaskById(taskId)

            // Assert
            assertTrue(result.isFailure)
            assertEquals(exception, result.exceptionOrNull())
            coVerify { remoteDataSource.getTaskById(taskId) }
        }
    }