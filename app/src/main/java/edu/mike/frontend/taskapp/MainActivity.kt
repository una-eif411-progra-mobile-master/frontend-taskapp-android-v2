package edu.mike.frontend.taskapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import java.util.Date
import java.util.Locale

/**
 * MainActivity is the entry point of the application.
 * It sets up the content view using Jetpack Compose and observes the task list from the ViewModel.
 *
 * Students should note that this activity does not use XML for layout.
 * Instead, the UI is built using Jetpack Compose components.
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
    // Observe the task list using collectAsState, so the UI updates automatically when the data changes
    val taskList by taskViewModel.taskList.collectAsState()

    // Get the app name and title from the resources
    val appName = stringResource(id = R.string.app_name)
    val appTitle = stringResource(id = R.string.app_title)

    // Surface is used to provide a background for the UI elements
    Surface(
        modifier = Modifier
            .fillMaxSize()  // Fill the available screen space
            .padding(0.dp)  // No padding for the surface
    ) {
        Column {
            // Display the app name as a large, centered title
            Text(
                text = appName,
                fontSize = 34.sp,  // Set the font size to 34sp
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()  // Make the Text take up the full width of the screen
                    .height(60.dp)   // Set the height of the Text
                    .background(Color.Blue)  // Set the background color to blue
                    .padding(8.dp)   // Add padding inside the Text
            )
            Spacer(modifier = Modifier.height(10.dp))  // Add space between the texts
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

            // LazyColumn is used to create a scrollable list of tasks
            // IMPORTANT: In the old version this was a RecyclerView
            LazyColumn(
                modifier = Modifier.fillMaxSize()  // Fill the screen
            ) {
                // Use items to iterate over the task list and display each task using the TaskItem composable
                items(taskList) { task ->
                    TaskItem(task = task)
                }
            }
        }
    }
}

/**
 * TaskItem is a composable function that displays the details of a task.
 *
 * Each task includes a title, notes, creation and due dates, priority, and status.
 * Students should pay attention to how data is passed into this function and how it's displayed.
 *
 * @param task The Task object to display.
 */
@Composable
fun TaskItem(task: Task) {
    // Format the dates for display using SimpleDateFormat
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val createDate = dateFormatter.format(task.createDate)
    val dueDate = dateFormatter.format(task.dueDate)

    // Column is used to vertically arrange the task details
    Column(
        modifier = Modifier
            .fillMaxWidth()  // Make the Column take up the full width of the screen
            .padding(8.dp)   // Add padding around the task item
            .background(Color.LightGray)  // Set the background color to light gray
            .padding(8.dp)   // Add padding inside the task item
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
        // Display the task's priority
        Text(
            text = "Priority: ${task.priority.label}",
            fontSize = 14.sp,
            color = Color.DarkGray,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        // Display the task's status
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
    // Create a sample task for preview purposes
    val sampleTask = Task(
        id = 1L,
        title = "Sample Task",
        notes = "Sample Notes for the task",
        createDate = Date(),
        dueDate = Date(),
        priority = Priority(1, "High"),
        status = Status(1, "Pending")
    )

    // Display the TaskItem using the sample data
    Surface {
        TaskItem(task = sampleTask)
    }
}