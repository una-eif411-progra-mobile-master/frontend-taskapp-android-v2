package edu.mike.frontend.taskapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.mike.frontend.taskapp.domain.model.Task
import edu.mike.frontend.taskapp.domain.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Sealed class representing the various states of a task operation.
 */
sealed class TaskState {
    /** Indicates an ongoing task operation */
    data object Loading : TaskState()

    /** Contains the successfully retrieved task */
    data class Success(val task: Task) : TaskState()

    /** Indicates no task is available */
    data object Empty : TaskState()

    /** Contains an error message if the task operation fails */
    data class Error(val message: String) : TaskState()
}

/**
 * ViewModel responsible for managing task-related UI state and business logic.
 *
 * @property repository Repository interface for task operations
 */
class TaskViewModel(
    private val repository: TaskRepository
) : ViewModel() {

    private val _task = MutableStateFlow<TaskState>(TaskState.Empty)
    val task: StateFlow<TaskState> = _task

    private val _selectedTask = MutableStateFlow<Task?>(null)
    val selectedTask: StateFlow<Task?> = _selectedTask

    private val _taskList = MutableStateFlow<List<Task>>(emptyList())
    val taskList: StateFlow<List<Task>> = _taskList

    /**
     * Finds a task by its ID and updates the [selectedTask] state.
     *
     * @param taskId The ID of the task to find
     */
    fun selectTaskById(taskId: Long) {
        viewModelScope.launch {
            repository.findTaskById(taskId)
                .onSuccess { task ->
                    _selectedTask.value = task
                }
                .onFailure { exception ->
                    Log.e("TaskViewModel", "Error fetching task by ID: ${exception.message}")
                }
        }
    }

    /**
     * Retrieves all available tasks and updates the [taskList] state.
     * Should be called when the ViewModel is initialized or when a refresh is needed.
     */
    fun findAllTasks() {
        viewModelScope.launch {
            repository.findAllTasks()
                .onSuccess { tasks ->
                    Log.d("TaskViewModel", "Total Tasks: ${tasks.size}")
                    _taskList.value = tasks
                }
                .onFailure { exception ->
                    Log.e("TaskViewModel", "Failed to fetch tasks: ${exception.message}")
                }
        }
    }
}