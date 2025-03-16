package edu.mike.frontend.taskapp.presentation.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import edu.mike.frontend.taskapp.domain.model.Task
import edu.mike.frontend.taskapp.presentation.ui.components.TaskMetadata
import edu.mike.frontend.taskapp.viewmodel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * TaskDetailScreen is a composable function that displays the details of a specific task.
 *
 * This screen retrieves a task based on the provided taskId and presents all its details in a
 * structured format using Material Design components. It handles both the successful task retrieval
 * and error states when a task is not found.
 *
 * Key Jetpack Compose concepts demonstrated:
 * - State observation with collectAsState
 * - Conditional UI rendering based on data presence
 * - Material 3 component usage (Card, Button, etc.)
 * - Accessibility features through semantics
 * - Side effects handling with LaunchedEffect
 *
 * @param taskId The unique identifier of the task to display, retrieved from navigation parameters
 * @param taskViewModel The ViewModel that provides access to task data and business logic
 * @param navController Navigation controller used to handle screen transitions
 * @param paddingValues Padding values typically provided by a Scaffold, applied to the overall layout
 */
@Composable
fun TaskDetailScreen(
    taskId: Long,
    taskViewModel: TaskViewModel,
    navController: NavController,
    paddingValues: PaddingValues
) {
    // Use LaunchedEffect to fetch the task when the screen is first composed
    // or when taskId changes, preventing unnecessary reloads
    LaunchedEffect(taskId) {
        taskViewModel.selectTaskById(taskId)
    }

    val task by taskViewModel.selectedTask.collectAsState()
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    // Apply paddingValues directly to content
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        task?.let {
            TaskDetailContent(
                task = it,
                dateFormatter = dateFormatter,
                onBackClick = { navController.navigateUp() }
            )
        } ?: TaskNotFoundContent(
            onBackClick = { navController.navigateUp() }
        )
    }
}

/**
 * Displays detailed information about a task in a card layout.
 *
 * This composable is responsible for organizing and presenting all the details
 * of a task in a structured and visually appealing manner. It uses Material Design
 * components and follows accessibility best practices.
 *
 * @param task The task object containing all the relevant details to display
 * @param dateFormatter Formatter to convert Date objects to readable strings
 * @param onBackClick Callback to handle navigation back to the task list
 */
@Composable
private fun TaskDetailContent(
    task: Task,
    dateFormatter: SimpleDateFormat,
    onBackClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .semantics { contentDescription = "Task details for ${task.title}" },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header section
            Text(
                text = "Task Details",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .semantics { heading() }
            )

            // Task title
            Text(
                text = task.title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Optional task notes
            if (task.notes.isNotBlank()) {
                Text(
                    text = task.notes,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Task metadata section
            Text(
                text = "Task Information",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Metadata fields using the reusable TaskMetadata component
            TaskMetadata(
                label = "Created On:",
                value = dateFormatter.format(task.createdDate),
                modifier = Modifier.padding(bottom = 4.dp)
            )

            TaskMetadata(
                label = "Due On:",
                value = dateFormatter.format(task.dueDate),
                modifier = Modifier.padding(bottom = 4.dp)
            )

            TaskMetadata(
                label = "Priority:",
                value = task.priority.label,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            TaskMetadata(
                label = "Status:",
                value = task.status.label,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            // Back button with icon
            Button(
                onClick = onBackClick,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
                Text(
                    text = "Back to Task List",
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

/**
 * Displays an error message when the requested task cannot be found.
 *
 * This composable provides feedback to the user when a task ID is invalid or
 * the task has been deleted. It uses the error-themed colors from the Material theme
 * to visually indicate an error state.
 *
 * @param onBackClick Callback to handle navigation back to the task list
 */
@Composable
private fun TaskNotFoundContent(onBackClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .semantics { contentDescription = "Task not found error message" },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Task Not Found",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onErrorContainer
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "The requested task could not be found. It may have been deleted or the ID is invalid.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer
            )

            Button(
                onClick = onBackClick,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(text = "Go Back")
            }
        }
    }
}