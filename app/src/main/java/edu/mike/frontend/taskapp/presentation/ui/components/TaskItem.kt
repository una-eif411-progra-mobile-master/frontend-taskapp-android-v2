package edu.mike.frontend.taskapp.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import edu.mike.frontend.taskapp.data.model.Task
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * TaskItem is a composable that displays an individual task in a card format.
 *
 * This component presents the task in a visually appealing card with proper hierarchy
 * highlighting important information like the title, while providing supporting details
 * such as notes, dates, priority and status in a structured format.
 *
 * The card is interactive, allowing users to select a task for detailed viewing or editing.
 * It includes proper accessibility support with semantic descriptions.
 *
 * @param task The task object containing all data to be displayed
 * @param onClick Callback function that is triggered when the task item is clicked
 * @param modifier Optional modifier for customizing the component's layout and appearance
 */
@Composable
fun TaskItem(
    task: Task,
    onClick: (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    // Format dates for display using a more user-friendly format
    val dateFormatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    // Handle potential null dates gracefully
    val createdDate = task.createdDate?.let { dateFormatter.format(it) } ?: "Not specified"
    val dueDate = task.dueDate?.let { dateFormatter.format(it) } ?: "No due date"

    // Prepare accessibility description
    val taskDescription =
        "Task: ${task.title}, Priority: ${task.priority.label}, Status: ${task.status.label}"

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick(task) }
            .semantics { contentDescription = taskDescription },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Task title with proper styling
            Text(
                text = task.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            // Optional notes section if available
            if (!task.notes.isNullOrBlank()) {
                Text(
                    text = task.notes,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // Task metadata displayed in a consistent format
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

/**
 * A helper composable that displays a label-value pair used for task metadata.
 *
 * This component creates a consistent layout for displaying metadata fields with
 * proper styling and spacing. It helps maintain visual consistency across different
 * task-related views in the application.
 *
 * @param label The descriptive text label for the metadata field
 * @param value The actual value of the metadata field
 * @param modifier Optional modifier for customizing the layout
 */
@Composable
fun TaskMetadata(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}