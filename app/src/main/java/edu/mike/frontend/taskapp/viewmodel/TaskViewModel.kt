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
 * This class represents the ViewModel for managing tasks.
 *
 * Features:
 * - StateFlow for reactive state management
 * - Coroutine integration with viewModelScope
 * - Separation of mutable and immutable state
 * - Random task selection functionality
 * - Full task list management
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

    // Function to find and set the selected task by its ID
    fun selectTaskById(taskId: Long) {
        viewModelScope.launch {
            val task = TaskProvider.findTaskById(taskId)
            _selectedTask.value = task
        }
    }

    /**
     * Fetches a random task from the TaskProvider.
     *
     * This function:
     * 1. Sets the task state to Loading
     * 2. Generates a random position (1-10)
     * 3. Retrieves the task from the provider
     * 4. Updates both task state and selectedTask
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