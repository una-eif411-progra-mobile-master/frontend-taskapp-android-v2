package edu.mike.frontend.taskapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.mike.frontend.taskapp.domain.model.Task
import edu.mike.frontend.taskapp.domain.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Sealed class representing the various states of a task operation.
 * Used to track the loading, success, error, and empty states for task operations.
 */
sealed class TaskState {
    /**
     * Initial state before any operation has started
     */
    data object Initial : TaskState()

    /**
     * Indicates an operation is in progress
     */
    data object Loading : TaskState()

    /**
     * Contains successfully retrieved task data
     * @property task The retrieved task
     */
    data class Success(val task: Task) : TaskState()

    /**
     * Indicates no task data is available
     */
    data object Empty : TaskState()

    /**
     * Represents an error condition during a task operation
     * @property message The error message describing what went wrong
     */
    data class Error(val message: String) : TaskState()
}

/**
 * ViewModel responsible for managing task-related UI state and business logic.
 *
 * This ViewModel handles:
 * - Retrieving task lists and individual tasks
 * - Managing loading states during network operations
 * - Exposing task data via StateFlows for reactive UI updates
 * - Error handling for task operations
 *
 * @property repository Repository interface for task operations
 */
@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    // Task list state
    private val _taskList = MutableStateFlow<List<Task>>(emptyList())

    /**
     * Flow of the list of all tasks. Empty list means no tasks or loading.
     */
    val taskList = _taskList.asStateFlow()

    // Task detail state
    private val _task = MutableStateFlow<TaskState>(TaskState.Initial)

    /**
     * Flow representing the current state of task detail operations
     * (loading, success, error, etc.)
     */
    val task = _task.asStateFlow()

    // Selected task state
    private val _selectedTask = MutableStateFlow<Task?>(null)

    /**
     * Flow of the currently selected task, if any
     */
    val selectedTask = _selectedTask.asStateFlow()

    /**
     * Fetches all tasks from the repository and updates [taskList].
     * Any network errors are logged but not exposed to the UI.
     */
    fun findAllTasks() {
        viewModelScope.launch {
            _taskList.value = emptyList() // Clear before loading
            repository.findAllTasks()
                .onSuccess { tasks ->
                    _taskList.value = tasks
                }
                .onFailure { exception ->
                    Log.e("TaskViewModel", "Error fetching tasks: ${exception.message}")
                }
        }
    }

    /**
     * Fetches a specific task by ID and updates both [task] and [selectedTask] states.
     * Updates UI state to reflect loading, success, or error conditions.
     *
     * @param taskId The unique identifier of the task to retrieve
     */
    fun getTaskById(taskId: Long) {
        viewModelScope.launch {
            _task.value = TaskState.Loading
            repository.findTaskById(taskId)
                .onSuccess { task ->
                    _task.value = TaskState.Success(task)
                    _selectedTask.value = task
                }
                .onFailure { exception ->
                    _task.value = TaskState.Error(exception.message ?: "Unknown error")
                    Log.e("TaskViewModel", "Error fetching task by ID: ${exception.message}")
                }
        }
    }
}