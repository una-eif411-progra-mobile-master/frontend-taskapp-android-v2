package edu.mike.frontend.taskapp.presentation.ui.layout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import edu.mike.frontend.taskapp.R

/**
 * MainLayout is a composable that provides the standard layout structure for the application.
 *
 * This component establishes a consistent visual framework across all screens with:
 * - A prominent header displaying the app name with proper accessibility semantics
 * - A descriptive subtitle that reinforces the app's purpose
 * - A designated content area for screen-specific UI elements
 *
 * The layout automatically handles scaffold padding and applies Material Design 3 theming
 * for visual coherence throughout the application. It ensures that all screens maintain
 * the same branding and structure while allowing for unique content.
 *
 * @param paddingValues PaddingValues to be applied to the layout, typically from Scaffold
 * @param content The screen-specific content to be displayed within this layout
 */
@Composable
fun MainLayout(
    paddingValues: PaddingValues, content: @Composable () -> Unit
) {
    // Get string resources before using in semantics
    val appName = stringResource(id = R.string.app_name)
    val appTitle = stringResource(id = R.string.app_title)

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            // App header section with primary branding
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary,
                shadowElevation = 4.dp
            ) {
                Text(
                    text = appName,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 8.dp)
                        .semantics { heading() })
            }

            // App subtitle section
            Text(
                text = appTitle,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            )

            // Screen-specific content
            content()
        }
    }
}