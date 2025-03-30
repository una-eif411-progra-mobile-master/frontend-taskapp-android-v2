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
 */
sealed class TaskState {
    data object Initial : TaskState()

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
@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    // State definitions
    private val _taskList = MutableStateFlow<List<Task>>(emptyList())
    val taskList = _taskList.asStateFlow()

    private val _task = MutableStateFlow<TaskState>(TaskState.Initial)
    val task = _task.asStateFlow()

    private val _selectedTask = MutableStateFlow<Task?>(null)
    val selectedTask = _selectedTask.asStateFlow()

    // Make sure to update state in your methods
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