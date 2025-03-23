package edu.mike.frontend.taskapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import edu.mike.frontend.taskapp.data.datasource.TaskDataSourceImpl
import edu.mike.frontend.taskapp.data.mapper.PriorityMapper
import edu.mike.frontend.taskapp.data.mapper.StatusMapper
import edu.mike.frontend.taskapp.data.mapper.TaskMapper
import edu.mike.frontend.taskapp.data.repository.TaskRepositoryImpl
import edu.mike.frontend.taskapp.presentation.factory.TaskViewModelFactory
import edu.mike.frontend.taskapp.presentation.navigation.NavGraph
import edu.mike.frontend.taskapp.presentation.ui.components.BottomNavigationBar
import edu.mike.frontend.taskapp.presentation.ui.layout.MainLayout
import edu.mike.frontend.taskapp.presentation.ui.theme.TaskAppTheme
import edu.mike.frontend.taskapp.presentation.viewmodel.TaskViewModel

class MainActivity : ComponentActivity() {
    private val taskViewModel: TaskViewModel by viewModels {
        val priorityMapper = PriorityMapper()
        val statusMapper = StatusMapper()
        val taskMapper = TaskMapper(priorityMapper, statusMapper)
        val dataSource = TaskDataSourceImpl(taskMapper)
        val taskRepository = TaskRepositoryImpl(dataSource, taskMapper)
        TaskViewModelFactory(taskRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TaskAppTheme {
                MainScreen(taskViewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(taskViewModel: TaskViewModel) {
    val navController = rememberNavController()
    val taskListState by taskViewModel.taskList.collectAsState()
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        taskViewModel.findAllTasks()
    }

    LaunchedEffect(taskListState) {
        if (taskListState.isNotEmpty()) {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
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
        },
        bottomBar = {
            BottomNavigationBar(navController = navController, taskViewModel = taskViewModel)
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .semantics { contentDescription = "Loading tasks" },
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            MainLayout(paddingValues = paddingValues) {
                NavGraph(
                    navController = navController,
                    taskViewModel = taskViewModel,
                    paddingValues = PaddingValues(0.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    TaskAppTheme {
        PreviewMainScreen()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PreviewMainScreen() {
    Scaffold(
        topBar = {
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
    ) { paddingValues ->
        Text(
            text = "Preview: Navigation Content",
            modifier = Modifier.padding(paddingValues)
        )
    }
}