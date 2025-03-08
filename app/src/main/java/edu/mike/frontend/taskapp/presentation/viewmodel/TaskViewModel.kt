package edu.mike.frontend.taskapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.mike.frontend.taskapp.data.datasource.TaskProvider
import edu.mike.frontend.taskapp.data.model.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Sealed class representing the various states of a task operation.
 *
 * States:
 * - Loading: Indicates an ongoing task fetch operation.
 * - Success: Contains the successfully retrieved task.
 * - Empty: Indicates no task is available.
 */
sealed class TaskState {
    data object Loading : TaskState()
    data class Success(val task: Task) : TaskState()
    data object Empty : TaskState()
}

/**
 * ViewModel for managing task-related operations.
 *
 * Features:
 * - StateFlow for reactive state management.
 * - Coroutine integration with viewModelScope.
 * - Separation of mutable and immutable state.
 * - Task selection and full task list management.
 */
class TaskViewModel : ViewModel() {

    // MutableStateFlow to hold the current task state
    private val _task = MutableStateFlow<TaskState>(TaskState.Empty)
    val task: StateFlow<TaskState> get() = _task

    // StateFlow to hold the current task selected by ID
    private val _selectedTask = MutableStateFlow<Task?>(null)
    val selectedTask: StateFlow<Task?> get() = _selectedTask

    // StateFlow to hold the list of tasks
    private val _taskList = MutableStateFlow<List<Task>>(emptyList())
    val taskList: StateFlow<List<Task>> get() = _taskList

    /**
     * Finds and sets the selected task by its ID.
     *
     * @param taskId The ID of the task to retrieve.
     */
    fun selectTaskById(taskId: Long) {
        viewModelScope.launch {
            val task = _taskList.value.find { it.id == taskId } ?: TaskProvider.findTaskById(taskId)
            _selectedTask.value = task
        }
    }

    /**
     * Retrieves a random task from the TaskProvider.
     *
     * Steps:
     * 1. Sets the task state to Loading.
     * 2. Generates a random task ID.
     * 3. Retrieves the task from the provider.
     * 4. Updates the task state and selectedTask.
     */
    fun getTask() {
        viewModelScope.launch {
            _task.value = TaskState.Loading
            val position = (1L..10L).random() // Generate random Long ID from 1 to 10
            val task = TaskProvider.findTaskById(position)
            _task.value = task?.let { TaskState.Success(it) } ?: TaskState.Empty
            _selectedTask.value = task
        }
    }

    /**
     * Retrieves all available tasks from the TaskProvider.
     *
     * This function:
     * 1. Fetches the complete task list.
     * 2. Updates the taskList state with the retrieved data.
     */
    fun findAllTasks() {
        viewModelScope.launch {
            val taskList = TaskProvider.findAllTasks()
            _taskList.value = taskList
        }
    }
}