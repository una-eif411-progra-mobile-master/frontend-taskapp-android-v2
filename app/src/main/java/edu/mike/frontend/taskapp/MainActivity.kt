package edu.mike.frontend.taskapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import edu.mike.frontend.taskapp.presentation.navigation.NavGraph
import edu.mike.frontend.taskapp.presentation.ui.components.BottomNavigationBar
import edu.mike.frontend.taskapp.presentation.ui.layout.MainLayout
import edu.mike.frontend.taskapp.presentation.ui.theme.TaskAppTheme
import edu.mike.frontend.taskapp.presentation.viewmodel.LoginViewModel
import edu.mike.frontend.taskapp.presentation.viewmodel.TaskViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val loginViewModel: LoginViewModel by viewModels()
    private val taskViewModel: TaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TaskAppTheme {
                MainScreen(taskViewModel, loginViewModel)
            }
        }
    }
}

@Composable
fun MainScreen(
    taskViewModel: TaskViewModel,
    loginViewModel: LoginViewModel
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

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
        bottomBar = {
            if (currentRoute != "login") {
                BottomNavigationBar(navController = navController, taskViewModel = taskViewModel)
            }
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
                    loginViewModel = loginViewModel,
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
        Scaffold { paddingValues ->
            MainLayout(paddingValues = paddingValues) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Task List Content Preview",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}