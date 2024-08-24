package edu.mike.frontend.taskapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.mike.frontend.taskapp.model.Priority
import edu.mike.frontend.taskapp.model.Status
import edu.mike.frontend.taskapp.model.Task
import edu.mike.frontend.taskapp.viewmodel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.*

/**
 * MainActivity is the entry point of the application.
 * It sets up the content view using Jetpack Compose and observes the task list from the ViewModel.
 */
class MainActivity : ComponentActivity() {

    // ViewModel instance for managing the task list and task data
    private val taskViewModel: TaskViewModel by viewModels()

    /**
     * onCreate is called when the activity is first created.
     * We are setting the content view to a Composable function (TaskApp) using setContent.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Ensure the task list is loaded when the activity starts
        taskViewModel.findAllTask()

        // Use Jetpack Compose to set the layout of the activity
        setContent {
            // Pass the ViewModel to the TaskApp composable
            TaskApp(taskViewModel)
        }
    }
}

/**
 * TaskApp is the main composable function that sets up the UI for the task list.
 *
 * This function observes the list of tasks from the ViewModel and displays it using LazyColumn,
 * which is a scrollable list. It also displays the app name and title.
 *
 * @param taskViewModel The ViewModel instance used to observe the task data.
 */
@Composable
fun TaskApp(taskViewModel: TaskViewModel) {
    // Collect the current task list from the ViewModel
    val taskList by taskViewModel.taskList.collectAsState()

    // State to track the selected task. Initially, select the first task if the list is not empty.
    var selectedTask by remember {
        mutableStateOf(taskList.firstOrNull())
    }

    // Get the app name and title from the resources
    val appName = stringResource(id = R.string.app_name)
    val appTitle = stringResource(id = R.string.app_title)

    // Display the UI
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp)
    ) {
        Column {
            // Display the app name and title
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

            // Always show the selected task information (or a placeholder if no task is selected)
            if (selectedTask != null) {
                SelectedTaskInfo(task = selectedTask!!)
            } else {
                Text(
                    text = "No task selected",
                    fontSize = 20.sp,
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // LazyColumn is used to create a scrollable list of tasks
            LazyColumn(
                modifier = Modifier.fillMaxSize()  // Fill the screen
            ) {
                // Use items to iterate over the task list and display each task using the TaskItem composable
                items(taskList) { task ->
                    TaskItem(task = task, onClick = { selectedTask = it })
                }
            }
        }
    }
}

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
    // Format the dates for display using SimpleDateFormat
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val createDate = dateFormatter.format(task.createDate)
    val dueDate = dateFormatter.format(task.dueDate)

    // Column is used to vertically arrange the task details
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.LightGray)
            .padding(8.dp)
            .clickable { onClick(task) }  // Make the task clickable
    ) {
        // Display the task title
        Text(
            text = "Title: ${task.title}",
            fontSize = 20.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 4.dp)  // Add padding below the title
        )
        // Display the task notes
        Text(
            text = "Notes: ${task.notes}",
            fontSize = 16.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        // Display the task's creation date
        Text(
            text = "Created On: $createDate",
            fontSize = 14.sp,
            color = Color.DarkGray,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        // Display the task's due date
        Text(
            text = "Due On: $dueDate",
            fontSize = 14.sp,
            color = Color.DarkGray,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        // Display the task's priority and status
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

/**
 * DefaultPreview provides a preview of the TaskItem composable.
 *
 * This preview function is useful in Android Studio for visualizing the layout of each task.
 * The data used here is hardcoded for preview purposes only.
 */
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val sampleTask = Task(
        id = 1L,
        title = "Sample Task",
        notes = "Sample Notes for the task",
        createDate = Date(),
        dueDate = Date(),
        priority = Priority(1, "High"),
        status = Status(1, "Pending")
    )

    Surface {
        TaskItem(task = sampleTask, onClick = {})
    }
}