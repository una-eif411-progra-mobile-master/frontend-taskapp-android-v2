package edu.mike.frontend.taskapp.presentation.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import edu.mike.frontend.taskapp.presentation.ui.components.TaskItem
import edu.mike.frontend.taskapp.presentation.ui.layout.MainLayout
import edu.mike.frontend.taskapp.presentation.viewmodel.TaskViewModel

/**
 * TaskListScreen is the composable function that sets up the UI for the task list.
 *
 * This function observes the list of tasks from the ViewModel and displays it using LazyColumn,
 * which is a scrollable list. It also displays the app name and title.
 *
 * @param taskViewModel The ViewModel instance used to observe the task data.
 * @param navController The NavController used for navigation between screens.
 */
@Composable
fun TaskListScreen(navController: NavController, taskViewModel: TaskViewModel) {
    val taskList by taskViewModel.taskList.collectAsState()

    MainLayout {
        if (taskList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "No tasks available.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                items(taskList) { task ->
                    TaskItem(task = task, onClick = {
                        navController.navigate("taskDetail/${task.id}")
                    })
                }
            }
        }
    }
}