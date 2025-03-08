package edu.mike.frontend.taskapp.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import edu.mike.frontend.taskapp.R

/**
 * BottomNavItem is a sealed class that represents the items in the bottom navigation bar.
 *
 * Each item in the bottom navigation bar has a unique route for navigation,
 * a string resource for the displayed text, and an icon for visual representation.
 *
 * @property route The route path used for navigation.
 * @property title The string resource ID for the displayed title.
 * @property icon The vector icon to display in the navigation bar.
 */
sealed class BottomNavItem(
    val route: String,
    @StringRes val title: Int,
    val icon: ImageVector
) {
    /**
     * Contains all the possible navigation routes as constants.
     */
    object Routes {
        const val TASK_LIST = "taskList"
        const val TASK_DETAIL = "taskDetail"
        const val SETTINGS = "settings"
    }

    /**
     * TaskList represents the navigation item for the task list screen.
     * This is the main screen where all tasks are displayed in a list format.
     */
    data object TaskList : BottomNavItem(
        Routes.TASK_LIST,
        R.string.task_list,
        Icons.AutoMirrored.Filled.List
    )

    /**
     * TaskDetail represents the navigation item for the task detail screen.
     * This screen displays detailed information about a selected task.
     */
    data object TaskDetail : BottomNavItem(
        Routes.TASK_DETAIL,
        R.string.task_detail,
        Icons.Filled.Info
    )

    /**
     * Settings represents the navigation item for the settings screen.
     * This screen allows users to configure application preferences.
     */
    data object Settings : BottomNavItem(
        Routes.SETTINGS,
        R.string.settings,
        Icons.Filled.Settings
    )

    companion object {
        /**
         * Returns a list of all bottom navigation items to be displayed in the navigation bar.
         */
        fun items() = listOf(TaskList, Settings)

        /**
         * Determines if the provided route matches any bottom navigation item route.
         *
         * @param route The route to check
         * @return True if the route matches a bottom nav item, false otherwise
         */
        fun isBottomNavRoute(route: String): Boolean {
            return route == Routes.TASK_LIST || route == Routes.SETTINGS
        }
    }
}