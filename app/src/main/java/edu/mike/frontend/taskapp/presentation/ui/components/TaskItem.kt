package edu.mike.frontend.taskapp.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.mike.frontend.taskapp.data.model.Task
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * TaskItem is a composable function that displays the details of a task.
 *
 * Each task includes a title and notes, and can be clicked to select the task.
 *
 * @param task The Task object to display.
 * @param onClick The function to call when the task is clicked.
 */
@Composable
fun TaskItem(task: Task, onClick: (Task) -> Unit) {
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val createdDate = dateFormatter.format(task.createdDate)
    val dueDate = dateFormatter.format(task.dueDate)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.LightGray)
            .padding(8.dp)
            .clickable { onClick(task) }
    ) {
        Text(
            text = "Title: ${task.title}",
            fontSize = 20.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = "Notes: ${task.notes}",
            fontSize = 16.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Created On: $createdDate",
            fontSize = 14.sp,
            color = Color.DarkGray,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = "Due On: $dueDate",
            fontSize = 14.sp,
            color = Color.DarkGray,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = "Priority: ${task.priority.label}",
            fontSize = 14.sp,
            color = Color.DarkGray,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = "Status: ${task.status.label}",
            fontSize = 14.sp,
            color = Color.DarkGray,
            modifier = Modifier.padding(bottom = 4.dp)
        )
    }
}