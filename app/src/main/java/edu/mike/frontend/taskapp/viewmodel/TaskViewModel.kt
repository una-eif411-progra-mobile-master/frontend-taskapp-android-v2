package edu.mike.frontend.taskapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.mike.frontend.taskapp.model.Task
import edu.mike.frontend.taskapp.model.TaskProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Sealed class representing the various states of a task operation.
 *
 * States:
 * - Loading: Indicates an ongoing task fetch operation
 * - Success: Contains the successfully retrieved task
 * - Empty: Indicates no task is available
 *
 * This sealed class ensures type-safe handling of all possible task states
 * in the UI layer through exhaustive when expressions.
 */
sealed class TaskState {
    data object Loading : TaskState()
    data class Success(val task: Task) : TaskState()
    data object Empty : TaskState()
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
 * @property taskList Immutable StateFlow exposing the list of all tasks
 */
class TaskViewModel : ViewModel() {

    // MutableStateFlow to hold the current task state
    private val _task = MutableStateFlow<TaskState>(TaskState.Empty)
    val task: StateFlow<TaskState> get() = _task

    // MutableStateFlow to hold the list of all tasks
    private val _taskList = MutableStateFlow<List<Task>>(emptyList())
    val taskList: StateFlow<List<Task>> get() = _taskList

    /**
     * Fetches a random task from the TaskProvider.
     *
     * This function:
     * 1. Sets the task state to Loading
     * 2. Generates a random position (0-9)
     * 3. Retrieves the task from the provider
     * 4. Updates the task state with Success or Empty
     */
    fun getTask() {
        viewModelScope.launch {
            _task.value = TaskState.Loading
            val position = (0..9).random()
            val task = TaskProvider.findTaskById(position)
            _task.value = task?.let { TaskState.Success(it) } ?: TaskState.Empty
        }
    }

    /**
     * Retrieves all available tasks from the TaskProvider.
     *
     * This function:
     * 1. Fetches the complete task list
     * 2. Updates the taskList state with the retrieved data
     */
    fun findAllTasks() {
        viewModelScope.launch {
            val taskList = TaskProvider.findAllTasks()
            _taskList.value = taskList
        }
    }
}