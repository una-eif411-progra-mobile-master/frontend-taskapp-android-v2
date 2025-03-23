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
 * It creates a consistent visual hierarchy with a branded header section containing the
 * application name and subtitle, followed by the screen-specific content. This component
 * handles proper padding and accessibility concerns while applying Material Design principles.
 *
 * @param paddingValues PaddingValues to be applied to the layout, typically from Scaffold
 * @param content The screen-specific content to be displayed within this layout
 */
@Composable
fun MainLayout(
    paddingValues: PaddingValues,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            // Screen-specific content
            content()
        }
    }
}