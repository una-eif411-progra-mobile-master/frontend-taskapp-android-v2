package edu.mike.frontend.taskapp.presentation.ui.components


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import edu.mike.frontend.taskapp.presentation.navigation.BottomNavItem
import edu.mike.frontend.taskapp.presentation.viewmodel.TaskViewModel

/**
 * A composable that implements the bottom navigation bar for the application.
 *
 * The bottom navigation bar provides access to the main destinations in the app:
 * - Task List: The main screen displaying all tasks
 * - Settings: Application configuration options
 *
 * Navigation between these destinations is handled with proper back stack management,
 * ensuring that users can navigate through the app in an intuitive way.
 *
 * @param navController The navigation controller that manages app navigation
 * @param taskViewModel The view model that provides access to task data
 */
@Composable
fun BottomNavigationBar(navController: NavController, taskViewModel: TaskViewModel) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        BottomNavItem.items().forEach { item ->
            val isSelected = currentDestination?.hierarchy?.any {
                it.route == item.route
            } ?: false

            // Get string resource outside of the semantics block
            val itemTitle = stringResource(id = item.title)
            val itemDescription = if (isSelected) {
                "$itemTitle, selected"
            } else {
                "$itemTitle, not selected"
            }

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = null
                    )
                },
                label = {
                    Text(
                        text = itemTitle,
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                selected = isSelected,
                onClick = {
                    navController.navigate(item.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination
                        launchSingleTop = true
                        // Restore state when navigating back
                        restoreState = true
                    }
                },
                modifier = Modifier.semantics {
                    contentDescription = itemDescription
                }
            )
        }
    }
}