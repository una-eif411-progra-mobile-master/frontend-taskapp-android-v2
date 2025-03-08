package edu.mike.frontend.taskapp.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import edu.mike.frontend.taskapp.presentation.ui.components.TaskItem
import edu.mike.frontend.taskapp.presentation.ui.layout.MainLayout
import edu.mike.frontend.taskapp.presentation.viewmodel.TaskViewModel

/**
 * TaskListScreen is a composable function that displays the list of available tasks.
 *
 * This screen presents all tasks in a scrollable list format, with each task displayed as a
 * clickable card. It handles both the populated state (displaying tasks) and empty state (displaying
 * a message when no tasks are available). When a task is clicked, the user is navigated to the
 * task detail screen for that specific task.
 *
 * @param navController The navigation controller used to handle screen transitions
 * @param taskViewModel The ViewModel that provides access to task data and business logic
 * @param paddingValues Padding values typically provided by a Scaffold, applied to the layout
 */
@Composable
fun TaskListScreen(
    navController: NavController,
    taskViewModel: TaskViewModel,
    paddingValues: PaddingValues
) {
    // Observe the task list from the ViewModel
    val taskList by taskViewModel.taskList.collectAsState()

    MainLayout(paddingValues = paddingValues) {
        if (taskList.isEmpty()) {
            // Display a message if the task list is empty
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .semantics {
                        contentDescription = "Empty task list notification"
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No tasks available",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            }
        } else {
            // Populated state - display the list of tasks
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)  // Adding padding if needed
            ) {
                items(
                    items = taskList,
                    key = { task -> task.id } // Using stable keys improves performance
                ) { task ->
                    TaskItem(
                        task = task,
                        onClick = {
                            navController.navigate("taskDetail/${task.id}")
                        }
                    )
                }
            }
        }
    }
}