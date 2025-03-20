package edu.mike.frontend.taskapp.presentation.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import edu.mike.frontend.taskapp.domain.repository.TaskRepository
import edu.mike.frontend.taskapp.presentation.viewmodel.TaskViewModel

/**
 * Factory for creating TaskViewModel instances with the required dependencies.
 * Provides the repository dependency to the ViewModel through the Factory pattern.
 *
 * @param taskRepository The repository that provides data access methods for tasks
 */
class TaskViewModelFactory(
    private val taskRepository: TaskRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(taskRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}