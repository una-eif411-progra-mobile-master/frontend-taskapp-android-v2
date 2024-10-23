package edu.mike.frontend.taskapp.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import edu.mike.frontend.taskapp.R

/**
 * BottomNavItem is a sealed class that represents the items in the bottom navigation bar.
 *
 * Each item has a route, a title resource ID, and an icon.
 *
 * @property route The route associated with the navigation item.
 * @property title The string resource ID for the title of the navigation item.
 * @property icon The icon to display for the navigation item.
 */
sealed class BottomNavItem(
    val route: String,
    @StringRes val title: Int,
    val icon: ImageVector
) {
    /**
     * TaskList represents the navigation item for the task list screen.
     */
    object TaskList : BottomNavItem("taskList", R.string.task_list, Icons.AutoMirrored.Filled.List)

    /**
     * TaskDetail represents the navigation item for the task detail screen.
     */
    object TaskDetail : BottomNavItem("taskDetail", R.string.task_detail, Icons.Filled.Info)

    /**
     * Settings represents the navigation item for the settings screen.
     */
    object Settings : BottomNavItem("settings", R.string.settings, Icons.Filled.Settings)

    /**
     * Logout represents the navigation item for the logout functionality.
     */
    object Logout : BottomNavItem("logout", R.string.logout, Icons.AutoMirrored.Filled.ExitToApp)
}