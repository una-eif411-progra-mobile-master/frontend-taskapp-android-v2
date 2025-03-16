package edu.mike.frontend.taskapp.presentation.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import edu.mike.frontend.taskapp.R
import edu.mike.frontend.taskapp.domain.model.Task
import edu.mike.frontend.taskapp.presentation.navigation.NavRoutes
import edu.mike.frontend.taskapp.presentation.ui.components.TaskItem
import edu.mike.frontend.taskapp.presentation.viewmodel.TaskState
import edu.mike.frontend.taskapp.presentation.viewmodel.TaskViewModel

/**
 * TaskListScreen is a composable function that displays the list of available tasks.
 *
 * This screen presents all tasks in a scrollable list format, with each task displayed as a
 * clickable card. It handles loading, empty, and error states appropriately. Users can refresh
 * the task list using pull-to-refresh gesture.
 *
 * Key Jetpack Compose concepts demonstrated:
 * - State management with collectAsState and remember
 * - Pull-to-refresh implementation with PullRefreshIndicator
 * - Conditional rendering based on state
 * - Reusable composable functions for different UI states
 * - Proper content description for accessibility
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
    val taskList by taskViewModel.taskList.collectAsState()
    val taskState by taskViewModel.task.collectAsState()

    LaunchedEffect(Unit) {
        taskViewModel.findAllTasks()
    }

    var isRefreshing by remember { mutableStateOf(false) }

    // Update refreshing state based on task state
    isRefreshing = taskState is TaskState.Loading

    @OptIn(androidx.compose.material.ExperimentalMaterialApi::class)
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            taskViewModel.findAllTasks()
        }
    )

    @OptIn(androidx.compose.material.ExperimentalMaterialApi::class)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .pullRefresh(pullRefreshState)
    ) {
        when {
            // Show loading indicator when list is empty and loading
            taskList.isEmpty() && taskState is TaskState.Loading -> {
                LoadingContent()
            }
            // Show empty state message
            taskList.isEmpty() -> {
                EmptyContent()
            }
            // Show the task list
            else -> {
                TaskListContent(
                    taskList = taskList,
                    navController = navController
                )
            }
        }

        @OptIn(androidx.compose.material.ExperimentalMaterialApi::class)
        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            backgroundColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

/**
 * Displays a loading indicator centered on the screen.
 *
 * This composable is shown when tasks are being fetched from the data source.
 * It demonstrates proper use of:
 * - Box layout with center alignment
 * - CircularProgressIndicator for loading state
 * - Semantic properties for accessibility
 */
@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.semantics {
                contentDescription = "Loading tasks"
            }
        )
    }
}

/**
 * Displays a message when no tasks are available.
 *
 * This composable is shown when the task list is empty.
 * It demonstrates:
 * - Text styling with MaterialTheme
 * - String resources usage
 * - Proper semantics for accessibility
 * - TextAlign for centered text
 */
@Composable
private fun EmptyContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(id = R.string.no_tasks_available),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(16.dp)
                .semantics {
                    contentDescription = "Empty task list message"
                }
        )
    }
}

/**
 * Displays a list of tasks in a scrollable column.
 *
 * This composable is shown when tasks are available to display.
 * It demonstrates:
 * - LazyColumn for efficient scrolling lists
 * - Items with unique keys for better performance
 * - Click handling for navigation
 * - Proper semantics for list accessibility
 *
 * @param taskList The list of tasks to display
 * @param navController The navigation controller used to navigate to task details
 */
@Composable
private fun TaskListContent(
    taskList: List<Task>,
    navController: NavController
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 8.dp)
            .semantics {
                contentDescription = "Task list with ${taskList.size} tasks"
            }
    ) {
        items(
            items = taskList,
            key = { task -> task.id ?: 0 }
        ) { task ->
            TaskItem(
                task = task,
                onClick = {
                    task.id?.let { id ->
                        navController.navigate(NavRoutes.TaskDetail.createRoute(id))
                    }
                }
            )
        }
    }
}