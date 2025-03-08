package edu.mike.frontend.taskapp.presentation.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import edu.mike.frontend.taskapp.presentation.ui.components.TaskMetadata
import edu.mike.frontend.taskapp.presentation.ui.layout.MainLayout
import edu.mike.frontend.taskapp.presentation.viewmodel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * TaskDetailScreen is a composable function that displays the details of a specific task.
 *
 * This screen retrieves a task based on the provided taskId and presents all its details in a
 * structured format using Material Design components. It handles both the successful task retrieval
 * and error states when a task is not found. The screen includes task information like title, notes,
 * dates, priority, and status, as well as a button to navigate back to the task list.
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
    // Fetch the task data based on the provided ID
    taskViewModel.selectTaskById(taskId)
    val task = taskViewModel.selectedTask.collectAsState().value
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    MainLayout(paddingValues = paddingValues) {
        task?.let {
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

                    // Title section
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Notes section - handle null or empty notes
                    if (!task.notes.isNullOrBlank()) {
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

                    Spacer(modifier = Modifier.height(16.dp))

                    // Navigation button
                    Button(
                        onClick = { navController.navigateUp() },
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                        Text(
                            text = "Back to Task List",
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        } ?: run {
            // Error state when task is not found
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
                        onClick = { navController.navigateUp() },
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Text(text = "Go Back")
                    }
                }
            }
        }
    }
}