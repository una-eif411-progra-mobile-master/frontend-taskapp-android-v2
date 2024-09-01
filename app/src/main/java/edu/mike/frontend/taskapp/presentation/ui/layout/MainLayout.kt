package edu.mike.frontend.taskapp.presentation.ui.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.mike.frontend.taskapp.R

/**
 * MainLayout is a composable function that sets up the main layout of the application.
 *
 * This layout includes a common app bar or header and a content area that changes based on the current screen.
 *
 * @param content The composable content to display within the main layout.
 */
@Composable
fun MainLayout(content: @Composable () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp)
    ) {
        Column {
            // Common app bar or header
            val appName = stringResource(id = R.string.app_name)
            val appTitle = stringResource(id = R.string.app_title)

            Text(
                text = appName,
                fontSize = 34.sp,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth() // Use fillMaxWidth instead of fillMaxSize
                    .background(Color.Blue)
                    .padding(8.dp)
            )
            Text(
                text = appTitle,
                fontSize = 20.sp,
                color = Color.Gray,
                modifier = Modifier
                    .fillMaxWidth() // Use fillMaxWidth instead of fillMaxSize
                    .padding(8.dp)
                    .padding(bottom = 20.dp)
            )

            // Spacer or any other common UI elements can be added here

            // Content that changes based on the current screen
            content()
        }
    }
}