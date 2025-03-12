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
import edu.mike.frontend.taskapp.viewmodel.TaskState
import edu.mike.frontend.taskapp.viewmodel.TaskViewModel

/**
 * MainActivity is the entry point of the application.
 * It sets the content view to the TaskApp composable function.
 */
class MainActivity : ComponentActivity() {

    // ViewModel instance for managing UI-related data
    private val taskViewModel: TaskViewModel by viewModels()

    /**
     * Called when the activity is starting.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TaskApp(taskViewModel)
        }
    }
}

/**
 * TaskApp is a composable function that displays the task application UI.
 * @param taskViewModel The ViewModel instance for managing UI-related data.
 */
@Composable
fun TaskApp(taskViewModel: TaskViewModel) {
    val taskState by taskViewModel.task.collectAsState()

    val appName = stringResource(id = R.string.app_name)
    val appTitle = stringResource(id = R.string.app_title)

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .clickable { taskViewModel.getTask() },
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
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
            }
            item {
                TaskContent(taskState)
            }
        }
    }
}

/**
 * TaskContent is a composable function that displays the details of a task based on its state.
 * @param taskState The state representing the task.
 */
@Composable
fun TaskContent(taskState: TaskState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        when (taskState) {
            is TaskState.Loading -> {
                Text(
                    text = stringResource(id = R.string.loading),
                    fontSize = 20.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            is TaskState.Success -> {
                Text(
                    text = stringResource(id = R.string.task_title),
                    fontSize = 16.sp,
                    color = Color.Gray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp)
                )
                Text(
                    text = taskState.task.title,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
                Text(
                    text = stringResource(id = R.string.task_notes),
                    fontSize = 16.sp,
                    color = Color.Gray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp)
                )
                Text(
                    text = taskState.task.notes,
                    fontSize = 24.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
            }

            is TaskState.Empty -> {
                Text(
                    text = stringResource(id = R.string.no_task_available),
                    fontSize = 20.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

/**
 * DefaultPreview is a composable function that displays a preview of the TaskApp UI.
 */
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TaskApp(taskViewModel = TaskViewModel())
}