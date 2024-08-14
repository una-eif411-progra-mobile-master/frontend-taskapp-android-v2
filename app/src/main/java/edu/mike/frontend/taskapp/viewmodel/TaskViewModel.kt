package edu.mike.frontend.taskapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import edu.mike.frontend.taskapp.model.Task
import edu.mike.frontend.taskapp.model.TaskProvider

/**
 * This class represents the ViewModel for the Task
 */
class TaskViewModel : ViewModel() {

    private val _task = MutableStateFlow<Task?>(null)
    val task: StateFlow<Task?> get() = _task

    private val _taskList = MutableStateFlow<List<Task>>(emptyList())
    val taskList: StateFlow<List<Task>> get() = _taskList

    fun getTask() {
        viewModelScope.launch {
            val position = (0..9).random()
            val task = TaskProvider.findTaskById(position)
            task.let {
                _task.value = it
            }
        }
    }

    fun findAllTask() {
        viewModelScope.launch {
            val taskList = TaskProvider.findAllTask()
            _taskList.value = taskList
        }
    }
}