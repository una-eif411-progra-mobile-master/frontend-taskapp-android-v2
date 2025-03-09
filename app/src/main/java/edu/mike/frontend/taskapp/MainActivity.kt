package edu.mike.frontend.taskapp

import android.os.Bundle
import android.util.Log
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.mike.frontend.taskapp.model.Priority
import edu.mike.frontend.taskapp.model.Status
import edu.mike.frontend.taskapp.model.Task
import edu.mike.frontend.taskapp.ui.theme.TaskAppTheme
import edu.mike.frontend.taskapp.viewmodel.TaskState
import edu.mike.frontend.taskapp.viewmodel.TaskViewModel
import java.util.Date


/**
 * Main entry point of the Task Application.
 *
 * This activity serves as the primary UI container for the Task Application,
 * demonstrating the implementation of a modern Android application using:
 * - Jetpack Compose for declarative UI development
 * - MVVM architecture pattern for separation of concerns
 * - StateFlow for reactive state management
 * - Material3 design system
 *
 * @see ComponentActivity Base class for activities using Compose
 * @see TaskViewModel Manages UI state and business logic
 */
class MainActivity : ComponentActivity() {
    private val taskViewModel: TaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TaskAppTheme {
                TaskAppScreen(taskViewModel)
            }
        }
    }
}

/**
 * Main screen composable that serves as the root container for the application UI.
 *
 * Features:
 * - Reactive state management using StateFlow and collectAsState
 * - Material3 Scaffold implementation for consistent layout structure
 * - Separation of concerns with composable functions
 * - Proper state hoisting pattern
 *
 * @param taskViewModel ViewModel instance that manages the UI state and business logic
 */
@Composable
fun TaskAppScreen(taskViewModel: TaskViewModel) {
    val task by taskViewModel.task.collectAsState()
    val taskList by taskViewModel.taskList.collectAsState()

    LaunchedEffect(Unit) {
        taskViewModel.findAllTasks()
    }

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
 * Top app bar composable that displays the application title using Material3 design.
 *
 * Features:
 * - Material3 CenterAlignedTopAppBar implementation
 * - Consistent theming with app's color scheme
 * - Localized string resource usage
 * - Proper typography scaling
 */
@OptIn(ExperimentalMaterial3Api::class) // ExperimentalMaterial3Api is an annotation class that marks certain Material 3 components as experimental
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
 * Main content composable that manages the task display area and list.
 *
 * Features:
 * - Responsive layout using Column and weight modifiers
 * - State-based UI rendering
 * - Proper component composition
 * - Event handling through callbacks
 *
 * @param modifier Modifier for customizing layout and appearance
 * @param taskState Current state of the selected task (Loading/Success/Empty)
 * @param taskList Collection of all available tasks
 * @param onRefresh Callback triggered when a refresh is requested
 */
@Composable
fun TaskAppContent(
    modifier: Modifier = Modifier,
    taskState: TaskState,
    taskList: List<Task>,
    onRefresh: () -> Unit
) {

    SideEffect {
        Log.d("TaskApp", "The state has changed: $taskState")
    }

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
 * Card composable that displays task information based on its current state.
 *
 * Features:
 * - State-based content rendering
 * - Interactive feedback through clicks
 * - Material3 elevation and styling
 * - Proper error and loading state handling
 *
 * @param modifier Modifier for customizing layout and appearance
 * @param taskState Current state of the task (Loading/Success/Empty)
 * @param onRefresh Callback triggered when the card is clicked
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

/**
 * Displays a loading indicator when task data is being fetched.
 *
 * This composable shows a centered circular progress indicator to provide
 * visual feedback during task loading operations.
 */
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

/**
 * Displays the content of a successfully loaded task.
 *
 * Features:
 * - Structured layout with title and notes
 * - Consistent typography styling
 * - Proper spacing between elements
 *
 * @param task The task object containing data to display
 */
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

/**
 * Displays a message when no task is available.
 *
 * This composable shows a user-friendly message when the task state is empty,
 * providing clear feedback about the absence of task data.
 */
@Composable
fun TaskEmptyContent() {
    Text(
        text = "No task available",
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.padding(16.dp)
    )
}

/**
 * Displays a scrollable list of tasks.
 *
 * Features:
 * - Lazy loading for performance optimization
 * - Consistent spacing between list items
 * - Proper layout structuring
 *
 * @param tasks Collection of tasks to display in the list
 * @param modifier Modifier for customizing layout and appearance
 */
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

/**
 * Displays an individual task item in the task list.
 *
 * Features:
 * - Material card design with proper elevation
 * - Structured layout with title and notes
 * - Consistent typography and spacing
 *
 * @param task The task object containing data to display
 */
@Composable
fun TaskListItem(task: Task, onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable(onClick = onClick),
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


/**
 * Composable function that displays a list of tasks for selection.
 *
 * Features:
 * - LazyColumn for optimized list rendering
 * - TaskListItem composable for individual task display
 * - Proper state management for selected task
 *
 * @param taskList List of tasks to display
 * @param onTaskSelected Callback triggered when a task is selected
 */
@Composable
fun TaskSelectionScreen(taskList: List<Task>, onTaskSelected: (Task) -> Unit = {}) {
    var selectedTaskId by rememberSaveable { mutableStateOf<Long?>(null) }

    LazyColumn {
        items(taskList) { task ->
            TaskListItem(
                task = task,
                onClick = {
                    selectedTaskId = task.id
                    onTaskSelected(task)
                    Log.d("TaskApp", "Task selected: ${task.title} id: $selectedTaskId")
                }
            )
        }
    }
}

/**
 * Preview composable for the TaskCard component.
 *
 * Demonstrates how a task card appears with sample data in the Success state.
 * Shows formatting for task title, notes, with High priority and In Progress status.
 */
@Preview(showBackground = true)
@Composable
fun TaskCardPreview() {
    TaskAppTheme {
        TaskCard(
            taskState = TaskState.Success(
                Task(
                    id = 1L,
                    title = "Complete the app design",
                    notes = "Focus on creating a clean and intuitive user interface",
                    priority = Priority(1L, "High"),
                    status = Status(2L, "In Progress"),
                    createdDate = Date(),
                    dueDate = Date(System.currentTimeMillis() + 86400000) // one day later
                )
            ),
            onRefresh = {}
        )
    }
}

/**
 * Preview composable for the TaskListItem component.
 *
 * Shows how an individual task appears within the task list.
 * Demonstrates formatting for a Medium priority, Pending status task.
 */
@Preview(showBackground = true)
@Composable
fun TaskListItemPreview() {
    TaskAppTheme {
        TaskListItem(
            task = Task(
                id = 2L,
                title = "Research API integration",
                notes = "Look for best practices in RESTFul API consumption",
                priority = Priority(2L, "Medium"),
                status = Status(1L, "Pending"),
                createdDate = Date(),
                dueDate = Date(System.currentTimeMillis() + 172800000) // two days later
            )
        )
    }
}

/**
 * Preview composable for the complete TaskAppContent layout.
 *
 * Demonstrates the full application content area with:
 * - A featured task in Success state
 * - A list of sample tasks with varied priorities and statuses
 * - Realistic date handling with created and due dates
 *
 * This preview uses a fixed width of 320dp to simulate a common device width.
 */
@Preview(showBackground = true, widthDp = 320)
@Composable
fun TaskAppContentPreview() {
    val now = Date()
    val future1 = Date(now.time + 259200000) // three days later
    val future2 = Date(now.time + 432000000) // five days later

    TaskAppTheme {
        TaskAppContent(
            taskState = TaskState.Success(
                Task(
                    id = 3L,
                    title = "Write unit tests",
                    notes = "Ensure code coverage for all critical components",
                    priority = Priority(1L, "High"),
                    status = Status(2L, "In Progress"),
                    createdDate = now,
                    dueDate = future1
                )
            ),
            taskList = listOf(
                Task(
                    id = 3L,
                    title = "Write unit tests",
                    notes = "Ensure code coverage for all critical components",
                    priority = Priority(1L, "High"),
                    status = Status(2L, "In Progress"),
                    createdDate = now,
                    dueDate = future1
                ),
                Task(
                    id = 4L,
                    title = "Implement dark mode",
                    notes = "Add support for light/dark theme switching",
                    priority = Priority(3L, "Low"),
                    status = Status(1L, "Pending"),
                    createdDate = now,
                    dueDate = future2
                )
            ),
            onRefresh = {}
        )
    }
}