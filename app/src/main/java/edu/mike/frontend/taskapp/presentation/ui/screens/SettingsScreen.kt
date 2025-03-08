package edu.mike.frontend.taskapp.presentation.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import edu.mike.frontend.taskapp.presentation.ui.layout.MainLayout

/**
 * Settings screen that displays application configuration options.
 * This is a simple demonstration version with just a label.
 *
 * @param navController The NavController for handling navigation
 * @param paddingValues Padding values typically provided by a Scaffold
 */
@Composable
fun SettingsScreen(
    navController: NavController,
    paddingValues: PaddingValues
) {
    MainLayout(paddingValues = paddingValues) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .semantics {
                    contentDescription = "Settings screen"
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Settings Screen",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}