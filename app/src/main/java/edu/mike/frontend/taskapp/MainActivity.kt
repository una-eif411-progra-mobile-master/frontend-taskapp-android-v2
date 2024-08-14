package edu.mike.frontend.taskapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.mike.frontend.taskapp.model.Task
import edu.mike.frontend.taskapp.viewmodel.TaskViewModel

/**
 * MainActivity is the entry point of the application.
 * It sets up the content view using Jetpack Compose.
 */
class MainActivity : ComponentActivity() {

    // ViewModel instance for managing UI-related data
    private val taskViewModel: TaskViewModel by viewModels()

    // Called when the activity is starting
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Set the content of the activity to the TaskApp composable
            TaskApp(taskViewModel)
        }
    }
}

/**
 * TaskApp is the main composable function that sets up the UI.
 * It observes the task state from the ViewModel and displays the UI accordingly.
 *
 * @param taskViewModel The ViewModel instance to observe the task state.
 */
@Composable
fun TaskApp(taskViewModel: TaskViewModel) {
    // Collect the current task state from the ViewModel
    val task by taskViewModel.task.collectAsState()

    // Get the app name and title from the resources
    val appName = stringResource(id = R.string.app_name)
    val appTitle = stringResource(id = R.string.app_title)

    // Display the UI
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                // Trigger the getTask method when the user clicks the screen
                .clickable { taskViewModel.getTask() },
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                // Display the app name
                Text(
                    text = appName,
                    fontSize = 34.sp,
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .background(Color.Blue)
                        .padding(8.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                // Display the app title
                Text(
                    text = appTitle,
                    fontSize = 20.sp,
                    color = Color.Gray,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .padding(bottom = 20.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
            item {
                // Display the task content
                TaskContent(task)
            }
        }
    }
}

/**
 * TaskContent is a composable function that displays the details of a task.
 * If the task is null, it displays a message indicating no task is available.
 *
 * @param task The task to display.
 */
@Composable
fun TaskContent(task: Task?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        if (task != null) {
            // Display the task title
            Text(
                text = "Title:",
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            )
            Text(
                text = task.title,
                fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
            // Display the task notes
            Text(
                text = "Notes:",
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            )
            Text(
                text = task.notes,
                fontSize = 24.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
        } else {
            // Display a message if no task is available
            Text(
                text = "No task available",
                fontSize = 20.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/**
 * DefaultPreview is a composable function that provides a preview of the TaskApp.
 * It is used for UI development in Android Studio.
 */
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TaskApp(taskViewModel = TaskViewModel())
}