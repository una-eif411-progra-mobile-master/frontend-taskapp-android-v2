package edu.mike.frontend.taskapp.data.mapper

import edu.mike.frontend.taskapp.data.datasource.model.PriorityDto
import edu.mike.frontend.taskapp.data.datasource.model.StatusDto
import edu.mike.frontend.taskapp.data.datasource.model.TaskDto
import edu.mike.frontend.taskapp.domain.model.Priority
import edu.mike.frontend.taskapp.domain.model.Status
import edu.mike.frontend.taskapp.domain.model.Task
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Locale

class TaskMapperTest {

    private lateinit var taskMapper: TaskMapper
    private lateinit var priorityMapper: PriorityMapper
    private lateinit var statusMapper: StatusMapper
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    @Before
    fun setUp() {
        priorityMapper = PriorityMapper()
        statusMapper = StatusMapper()
        taskMapper = TaskMapper(priorityMapper, statusMapper)
    }

    @Test
    fun `mapToDomain should map TaskDto to Task`() {
        val taskDto = TaskDto(
            id = 1L,
            title = "Test Task",
            notes = "Test Notes",
            createdDate = dateFormat.parse("2023-10-01")!!,
            dueDate = dateFormat.parse("2023-10-10")!!,
            priority = PriorityDto(id = 1, label = "High"),
            status = StatusDto(id = 1, label = "Open")
        )

        val task = taskMapper.mapToDomain(taskDto)

        assertEquals(taskDto.id, task.id)
        assertEquals(taskDto.title, task.title)
        assertEquals(taskDto.notes, task.notes)
        assertEquals(taskDto.createdDate, task.createdDate)
        assertEquals(taskDto.dueDate, task.dueDate)
        assertEquals(taskDto.priority.id, task.priority.id)
        assertEquals(taskDto.priority.label, task.priority.label)
        assertEquals(taskDto.status.id, task.status.id)
        assertEquals(taskDto.status.label, task.status.label)
    }

    @Test
    fun `mapToDto should map Task to TaskDto`() {
        val task = Task(
            id = 1L,
            title = "Test Task",
            notes = "Test Notes",
            createdDate = dateFormat.parse("2023-10-01")!!,
            dueDate = dateFormat.parse("2023-10-10")!!,
            priority = Priority(id = 1, label = "High"),
            status = Status(id = 1, label = "Open")
        )

        val taskDto = taskMapper.mapToDto(task)

        assertEquals(task.id, taskDto.id)
        assertEquals(task.title, taskDto.title)
        assertEquals(task.notes, taskDto.notes)
        assertEquals(task.createdDate, taskDto.createdDate)
        assertEquals(task.dueDate, taskDto.dueDate)
        assertEquals(task.priority.id, taskDto.priority.id)
        assertEquals(task.priority.label, taskDto.priority.label)
        assertEquals(task.status.id, taskDto.status.id)
        assertEquals(task.status.label, taskDto.status.label)
    }
}