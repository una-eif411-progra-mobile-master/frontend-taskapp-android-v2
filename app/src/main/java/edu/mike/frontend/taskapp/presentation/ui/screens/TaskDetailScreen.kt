package edu.mike.frontend.taskapp.presentation.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import edu.mike.frontend.taskapp.presentation.ui.layout.MainLayout
import edu.mike.frontend.taskapp.presentation.viewmodel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.*

/**
 * TaskDetailScreen is a composable function that displays the details of a specific task.
 *
 * This screen shows the task's title, notes, creation date, due date, priority, and status.
 * It also includes a button to navigate back to the task list.
 *
 * @param taskId The ID of the task to display.
 * @param taskViewModel The ViewModel instance used to observe the task data.
 * @param navController The NavController used for navigation between screens.
 */
@Composable
fun TaskDetailScreen(taskId: Int, taskViewModel: TaskViewModel, navController: NavController) {
    taskViewModel.selectTaskById(taskId)
    val task = taskViewModel.selectedTask.collectAsState().value
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    MainLayout {
        task?.let {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(text = "Task Details", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))
                Text(text = "Title: ${task.title}", fontSize = 20.sp, modifier = Modifier.padding(bottom = 8.dp))
                Text(text = "Notes: ${task.notes}", fontSize = 16.sp, modifier = Modifier.padding(bottom = 8.dp))
                Text(text = "Created On: ${dateFormatter.format(task.createDate)}", fontSize = 14.sp, modifier = Modifier.padding(bottom = 4.dp))
                Text(text = "Due On: ${dateFormatter.format(task.dueDate)}", fontSize = 14.sp, modifier = Modifier.padding(bottom = 4.dp))
                Text(text = "Priority: ${task.priority.label}", fontSize = 14.sp, modifier = Modifier.padding(bottom = 4.dp))
                Text(text = "Status: ${task.status.label}", fontSize = 14.sp, modifier = Modifier.padding(bottom = 4.dp))

                // Button to go back to the task list
                Button(
                    onClick = { navController.navigateUp() },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text(text = "Back to Task List")
                }
            }
        } ?: run {
            Text(text = "Task not found", fontSize = 20.sp, modifier = Modifier.padding(16.dp))
        }
    }
}