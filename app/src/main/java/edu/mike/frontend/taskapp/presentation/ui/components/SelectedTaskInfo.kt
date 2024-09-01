package edu.mike.frontend.taskapp.presentation.ui.components

import androidx.compose.foundation.background
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

/**
 * SelectedTaskInfo is a composable that displays the details of the selected task.
 * This composable is shown above the task list when a task is selected.
 *
 * @param task The task whose details are displayed.
 */
@Composable
fun SelectedTaskInfo(task: Task) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.Yellow)
            .padding(16.dp)
    ) {
        Text(text = "Selected Task:", fontSize = 24.sp, color = Color.Black)
        Text(text = "Title: ${task.title}", fontSize = 20.sp, color = Color.Black)
        Text(text = "Notes: ${task.notes}", fontSize = 16.sp, color = Color.Gray)
    }
}