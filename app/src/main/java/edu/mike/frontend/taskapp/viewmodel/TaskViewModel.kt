package edu.mike.frontend.taskapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.mike.frontend.taskapp.model.Task
import edu.mike.frontend.taskapp.model.TaskProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for managing UI-related data in a lifecycle-conscious way.
 * It allows data to survive configuration changes such as screen rotations.
 */
class TaskViewModel : ViewModel() {

    // MutableStateFlow to hold the current task
    private val _task = MutableStateFlow<Task?>(null)
    val task: StateFlow<Task?> get() = _task

    // MutableStateFlow to hold the list of all tasks
    private val _taskList = MutableStateFlow<List<Task>>(emptyList())
    val taskList: StateFlow<List<Task>> get() = _taskList

    /**
     * Fetches a random task from the TaskProvider and updates the _task state.
     */
    fun getTask() {
        viewModelScope.launch {
            val position = (0..9).random()
            val task = TaskProvider.findTaskById(position)
            _task.value = task
        }
    }

    /**
     * Fetches all tasks from the TaskProvider and updates the _taskList state.
     */
    fun findAllTasks() {
        viewModelScope.launch {
            val taskList = TaskProvider.findAllTasks()
            _taskList.value = taskList
        }
    }
}