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
import edu.mike.frontend.taskapp.domain.model.Task
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * TaskItem is a composable function that displays the details of a task in a card format.
 *
 * Each task is displayed in a card with its title, notes (if available), and metadata like
 * creation date, due date, priority, and status. The card is clickable to allow user interaction.
 *
 * @param task The Task object containing all the data to be displayed.
 * @param onClick Callback function that is triggered when the task item is clicked.
 */
@Composable
fun TaskItem(task: Task, onClick: (Task) -> Unit) {
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val createdDate = dateFormatter.format(task.createdDate)
    val dueDate = dateFormatter.format(task.dueDate)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick(task) }
            .semantics { contentDescription = "Task: ${task.title}" },
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
            Text(
                text = task.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 4.dp)
            )

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
 * @param label The descriptive text label for the metadata field.
 * @param value The actual value of the metadata field.
 * @param modifier Optional modifier for customizing the layout.
 */
@Composable
fun TaskMetadata(  // Remove the 'private' keyword here
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