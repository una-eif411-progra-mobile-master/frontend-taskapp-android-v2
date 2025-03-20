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
import edu.mike.frontend.taskapp.presentation.viewmodel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Displays detailed information about a specific task.
 *
 * This screen observes the TaskViewModel's state to display task details or error states.
 * It implements proper state handling and error management through the repository pattern.
 *
 * @param taskId The unique identifier of the task to display
 * @param taskViewModel The ViewModel that manages task data and state
 * @param navController Navigation controller for screen transitions
 * @param paddingValues Padding values from the parent Scaffold
 */
@Composable
fun TaskDetailScreen(
    taskId: Long,
    taskViewModel: TaskViewModel,
    navController: NavController,
    paddingValues: PaddingValues
) {
    LaunchedEffect(taskId) {
        taskViewModel.selectTaskById(taskId)
    }

    val selectedTask by taskViewModel.selectedTask.collectAsState()
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        selectedTask?.let {
            TaskDetailContent(
                task = it,
                dateFormatter = dateFormatter,
                onBackClick = { navController.navigateUp() })
        } ?: TaskNotFoundContent(
            onBackClick = { navController.navigateUp() })
    }
}

/**
 * Displays the task details in a structured card layout.
 *
 * @param task The task to display
 * @param dateFormatter Formatter for displaying dates
 * @param onBackClick Callback for navigation
 */
@Composable
private fun TaskDetailContent(
    task: Task, dateFormatter: SimpleDateFormat, onBackClick: () -> Unit
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
            Text(
                text = "Task Details",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .semantics { heading() })

            Text(
                text = task.title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            if (task.notes.isNotBlank()) {
                Text(
                    text = task.notes,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            TaskMetadataSection(task, dateFormatter)

            Spacer(modifier = Modifier.weight(1f))

            BackButton(onBackClick)
        }
    }
}

/**
 * Displays task metadata information.
 *
 * @param task The task containing the metadata
 * @param dateFormatter Formatter for displaying dates
 */
@Composable
private fun TaskMetadataSection(task: Task, dateFormatter: SimpleDateFormat) {
    Text(
        text = "Task Information",
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(bottom = 8.dp)
    )

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
        label = "Priority:", value = task.priority.label, modifier = Modifier.padding(bottom = 4.dp)
    )

    TaskMetadata(
        label = "Status:", value = task.status.label, modifier = Modifier.padding(bottom = 4.dp)
    )
}

/**
 * Back button component with icon.
 *
 * @param onBackClick Callback for navigation
 */
@Composable
private fun BackButton(onBackClick: () -> Unit) {
    Button(
        onClick = onBackClick, modifier = Modifier.padding(top = 16.dp)
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back"
        )
        Text(
            text = "Back to Task List", modifier = Modifier.padding(start = 8.dp)
        )
    }
}

/**
 * Error content when task is not found.
 *
 * @param onBackClick Callback for navigation
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
                onClick = onBackClick, modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(text = "Go Back")
            }
        }
    }
}