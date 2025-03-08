package edu.mike.frontend.taskapp.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import edu.mike.frontend.taskapp.data.model.Task
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * A composable that displays detailed information about a selected task.
 *
 * This component creates a visually distinct card that prominently showcases
 * the selected task with its full details including title, notes, due date,
 * priority, and status. It applies Material Design 3 styling for visual consistency
 * throughout the application.
 *
 * The component automatically handles empty states for optional fields such as notes,
 * displaying appropriate placeholder text when needed. It also formats dates in a
 * user-friendly manner.
 *
 * @param task The task object containing all details to be displayed
 * @param modifier Optional modifier for customizing the component's layout and appearance
 */
@Composable
fun SelectedTaskInfo(
    task: Task,
    modifier: Modifier = Modifier
) {
    // Format dates for display
    val dateFormatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    val createdDate = task.createdDate?.let { dateFormatter.format(it) } ?: "Not specified"
    val dueDate = task.dueDate?.let { dateFormatter.format(it) } ?: "No due date"

    // Get readable string for task identifier
    val taskIdentifier = "Task #${task.id}"

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header section
            Text(
                text = taskIdentifier,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Title section
            Text(
                text = task.title,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Notes section
            if (!task.notes.isNullOrBlank()) {
                Text(
                    text = task.notes,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f),
                    maxLines = 5,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Metadata section
            TaskMetadata(
                label = "Created On:",
                value = createdDate,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            TaskMetadata(
                label = "Due On:",
                value = dueDate,
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
        }
    }
}