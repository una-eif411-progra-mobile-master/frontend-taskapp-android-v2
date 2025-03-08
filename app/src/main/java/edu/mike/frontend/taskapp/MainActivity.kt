package edu.mike.frontend.taskapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import edu.mike.frontend.taskapp.model.Task
import edu.mike.frontend.taskapp.ui.theme.TaskAppTheme
import edu.mike.frontend.taskapp.viewmodel.TaskState
import edu.mike.frontend.taskapp.viewmodel.TaskViewModel

/**
 * Main entry point of the Task Application.
 *
 * This activity demonstrates:
 * - Usage of Jetpack Compose for modern UI development
 * - Implementation of MVVM architecture pattern
 * - State management using ViewModels
 * - Proper activity lifecycle handling
 *
 * @see ComponentActivity
 * @see TaskViewModel
 */
class MainActivity : ComponentActivity() {
    private val taskViewModel: TaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        taskViewModel.findAllTasks()
        setContent {
            TaskAppTheme {
                TaskAppScreen(taskViewModel)
            }
        }
    }
}

/**
 * Main screen composable that serves as the container for all UI elements.
 *
 * This composable demonstrates:
 * - State management using StateFlow and collectAsState
 * - Implementation of Material3 Scaffold pattern
 * - Proper composition of UI components
 *
 * @param taskViewModel The ViewModel that manages the UI state
 */
@Composable
fun TaskAppScreen(taskViewModel: TaskViewModel) {
    val task by taskViewModel.task.collectAsState()
    val taskList by taskViewModel.taskList.collectAsState()

    Scaffold(
        topBar = { TaskAppTopBar() }
    ) { paddingValues ->
        TaskAppContent(
            modifier = Modifier.padding(paddingValues),
            taskState = task,
            taskList = taskList,
            onRefresh = { taskViewModel.getTask() }
        )
    }
}

/**
 * Top app bar composable that displays the application title.
 *
 * Demonstrates:
 * - Material3 TopAppBar implementation
 * - Proper theming and styling
 * - Resource management using stringResource
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskAppTopBar() {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.headlineMedium
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

/**
 * Main content composable that displays the task information and list.
 *
 * Demonstrates:
 * - Composable parameter passing
 * - State handling
 * - Layout composition
 * - Modifier usage
 *
 * @param modifier Modifier for styling and layout
 * @param taskState Current state of the selected task
 * @param taskList List of all available tasks
 * @param onRefresh Callback for refresh action
 */
@Composable
fun TaskAppContent(
    modifier: Modifier = Modifier,
    taskState: TaskState,
    taskList: List<Task>,
    onRefresh: () -> Unit
) {
    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = stringResource(id = R.string.app_title),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )

        TaskCard(
            taskState = taskState,
            onRefresh = onRefresh,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        TaskList(
            tasks = taskList,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
    }
}

/**
 * Card composable that displays task details based on its state.
 *
 * Demonstrates:
 * - State pattern implementation in UI
 * - Conditional rendering
 * - Material3 Card usage
 *
 * @param modifier Modifier for styling and layout
 * @param taskState Current state of the task
 * @param onRefresh Callback for refresh action
 */
@Composable
fun TaskCard(
    modifier: Modifier = Modifier,
    taskState: TaskState,
    onRefresh: () -> Unit
) {
    Card(
        modifier = modifier.clickable(onClick = onRefresh),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        when (taskState) {
            is TaskState.Loading -> TaskLoadingContent()
            is TaskState.Success -> TaskSuccessContent(task = taskState.task)
            is TaskState.Empty -> TaskEmptyContent()
        }
    }
}

@Composable
fun TaskLoadingContent() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun TaskSuccessContent(task: Task) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = task.title,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = task.notes,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun TaskEmptyContent() {
    Text(
        text = "No task available",
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.padding(16.dp)
    )
}

@Composable
fun TaskList(
    tasks: List<Task>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(tasks) { task ->
            TaskListItem(task = task)
        }
    }
}

@Composable
fun TaskListItem(task: Task) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = task.title,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = task.notes,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}