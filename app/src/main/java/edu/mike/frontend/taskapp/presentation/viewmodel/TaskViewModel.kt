package edu.mike.frontend.taskapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.mike.frontend.taskapp.domain.model.Task
import edu.mike.frontend.taskapp.domain.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * Sealed class representing the various states of a task operation.
 *
 * States:
 * - Loading: Indicates an ongoing task fetch operation
 * - Success: Contains the successfully retrieved task
 * - Empty: Indicates no task is available
 * - Error: Contains an error message if the task fetch operation fails
 *
 * This sealed class ensures type-safe handling of all possible task states
 * in the UI layer through exhaustive when expressions.
 */
sealed class TaskState {
    data object Loading : TaskState()
    data class Success(val task: Task) : TaskState()
    data object Empty : TaskState()
    data class Error(val message: String) : TaskState()
}

/**
 * ViewModel responsible for managing task-related UI state and business logic.
 *
 * Features:
 * - StateFlow for reactive state management
 * - Coroutine integration with viewModelScope
 * - Separation of mutable and immutable state
 * - Random task selection functionality
 * - Full task list management
 *
 * @property task Immutable StateFlow exposing the current task state
 * @property selectedTask Immutable StateFlow exposing the currently selected task
 * @property taskList Immutable StateFlow exposing the list of all tasks
 * @property repository Repository interface for task operations
 */
class TaskViewModel(
    private val repository: TaskRepository
) : ViewModel() {

    private val _task = MutableStateFlow<TaskState>(TaskState.Empty)
    val task: StateFlow<TaskState> get() = _task

    private val _selectedTask = MutableStateFlow<Task?>(null)
    val selectedTask: StateFlow<Task?> get() = _selectedTask

    private val _taskList = MutableStateFlow<List<Task>>(emptyList())
    val taskList: StateFlow<List<Task>> get() = _taskList

    /**
     * Retrieves a random task from the task list.
     *
     * - Shows loading state while retrieving the task.
     * - If tasks are available, selects a random one.
     * - If no tasks are available, sets the state to `Empty`.
     * - In case of error, sets the state to `Error`.
     */
    fun getTask() {
        viewModelScope.launch {
            _task.value = TaskState.Loading
            try {
                val taskList = repository.getTasks().first()
                if (taskList.isNotEmpty()) {
                    val position = (taskList.indices).random()
                    val task = taskList[position]
                    _task.value = TaskState.Success(task)
                    _selectedTask.value = task
                } else {
                    _task.value = TaskState.Empty
                }
            } catch (e: Exception) {
                _task.value = TaskState.Error("Failed to fetch task: ${e.message}")
            }
        }
    }

    /**
     * Finds a task by its ID and stores it in `selectedTask`.
     *
     * @param taskId The ID of the task to find
     * If the task doesn't exist, keeps `selectedTask` as `null`.
     */
    fun selectTaskById(taskId: Long) {
        viewModelScope.launch {
            try {
                val task = repository.getTaskById(taskId)
                _selectedTask.value = task
            } catch (e: Exception) {
                Log.e("TaskViewModel", "Error fetching task by ID: ${e.message}")
            }
        }
    }

    /**
     * Retrieves all available tasks and continuously updates the list.
     * Uses Flow to observe changes in the task list.
     */
    fun findAllTasks() {
        viewModelScope.launch {
            try {
                repository.getTasks().collect { tasks ->
                    Log.d("TaskViewModel", "Total Tasks: ${tasks.size}")
                    _taskList.value = tasks
                }
            } catch (e: Exception) {
                Log.e("TaskViewModel", "Failed to fetch tasks: ${e.message}")
            }
        }
    }
}