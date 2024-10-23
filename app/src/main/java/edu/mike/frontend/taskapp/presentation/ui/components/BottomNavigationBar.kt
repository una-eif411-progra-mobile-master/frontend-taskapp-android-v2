package edu.mike.frontend.taskapp.presentation.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import edu.mike.frontend.taskapp.navigation.BottomNavItem
import edu.mike.frontend.taskapp.presentation.viewmodel.LoginViewModel
import edu.mike.frontend.taskapp.presentation.viewmodel.TaskViewModel

/**
 * BottomNavigationBar is a composable function that sets up the bottom navigation bar for the application.
 *
 * This navigation bar allows users to switch between different screens such as the task list, task detail, and settings.
 *
 * @param navController The NavController used for navigation between screens.
 * @param taskViewModel The ViewModel instance used to observe the task data.
 * @param loginViewModel The ViewModel instance used to handle the login state and logout.
 */
@Composable
fun BottomNavigationBar(
    navController: NavController,
    taskViewModel: TaskViewModel,
    loginViewModel: LoginViewModel,  // Added loginViewModel to handle logout
    backgroundColor: Color = Color.Blue,
    selectedItemColor: Color = Color.White,  // White for selected items
    unselectedItemColor: Color = Color.Gray  // Gray for unselected items
) {
    val items = listOf(
        BottomNavItem.TaskList,
        BottomNavItem.TaskDetail,
        BottomNavItem.Settings,
        BottomNavItem.Logout
    )

    NavigationBar(
        containerColor = backgroundColor,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = stringResource(id = item.title)
                    )
                },
                label = {
                    Text(
                        text = stringResource(id = item.title),
                        textAlign = TextAlign.Center
                    )
                },
                selected = currentRoute == item.route,
                onClick = {
                    when (item) {
                        is BottomNavItem.TaskDetail -> {
                            val firstTask = taskViewModel.taskList.value.firstOrNull()
                            if (firstTask != null) {
                                navController.navigate("taskDetail/${firstTask.id}")
                            } else {
                                // Handle the case when there is no task
                                // Maybe show a Toast or SnackBar message to inform the user
                            }
                        }

                        is BottomNavItem.Logout -> {
                            // Handle logout action
                            loginViewModel.logout()
                            navController.navigate("login") {
                                popUpTo(0) { inclusive = true }  // Clears the entire back stack
                            }
                        }

                        else -> {
                            navController.navigate(item.route) {
                                navController.graph.startDestinationRoute?.let { route ->
                                    popUpTo(route) { saveState = true }
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,  // Set icons to white when selected
                    unselectedIconColor = Color.Gray,  // Set icons to gray when unselected
                    selectedTextColor = Color.White,  // Set text to white when selected
                    unselectedTextColor = Color.Gray  // Set text to gray when unselected
                )
            )
        }
    }
}