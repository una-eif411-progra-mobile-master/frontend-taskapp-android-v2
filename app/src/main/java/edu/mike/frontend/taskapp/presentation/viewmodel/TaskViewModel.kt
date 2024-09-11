package edu.mike.frontend.taskapp.presentation.viewmodel

import edu.mike.frontend.taskapp.data.repository.TaskRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.mike.frontend.taskapp.data.model.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * This class represents the ViewModel for managing tasks.
 * It uses Hilt to inject the edu.mike.frontend.taskapp.data.repository.TaskRepository and fetch data from the network.
 */
@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {

    // StateFlow to hold the current task selected by ID
    private val _selectedTask = MutableStateFlow<Task?>(null)
    val selectedTask: StateFlow<Task?> get() = _selectedTask

    // StateFlow to hold the list of tasks
    private val _taskList = MutableStateFlow<List<Task>>(emptyList())
    val taskList: StateFlow<List<Task>> get() = _taskList

    // Function to find and set the selected task by its ID
    fun selectTaskById(taskId: Int) {
        viewModelScope.launch {
            val task = taskRepository.getTaskById(taskId)
            _selectedTask.value = task // Handle null if task not found
        }
    }

    // Function to fetch and set all tasks from the repository
    fun findAllTasks() {
        viewModelScope.launch {
            val taskList = taskRepository.getAllTasks()
            _taskList.value = taskList ?: emptyList() // Handle empty list if no tasks found
        }
    }
}