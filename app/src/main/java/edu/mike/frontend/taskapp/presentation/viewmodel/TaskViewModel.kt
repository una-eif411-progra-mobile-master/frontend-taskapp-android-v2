package edu.mike.frontend.taskapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.mike.frontend.taskapp.data.datasource.TaskProvider
import edu.mike.frontend.taskapp.data.model.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * This class represents the ViewModel for managing tasks.
 */
class TaskViewModel : ViewModel() {

    // StateFlow to hold the current task selected by ID
    private val _selectedTask = MutableStateFlow<Task?>(null)
    val selectedTask: StateFlow<Task?> get() = _selectedTask

    // StateFlow to hold the list of tasks
    private val _taskList = MutableStateFlow<List<Task>>(emptyList())
    val taskList: StateFlow<List<Task>> get() = _taskList

    // Function to find and set the selected task by its ID
    fun selectTaskById(taskId: Int) {
        viewModelScope.launch {
            val task = taskList.value.find { it.id.toInt() == taskId }
            _selectedTask.value = task
        }
    }

    // Function to find and set a random task (for example usage)
    fun getTask() {
        viewModelScope.launch {
            val position = (0..9).random()
            val task = TaskProvider.findTaskById(position)
            _selectedTask.value = task
        }
    }

    // Function to find and set all tasks
    fun findAllTask() {
        viewModelScope.launch {
            val taskList = TaskProvider.findAllTask()
            _taskList.value = taskList
        }
    }
}