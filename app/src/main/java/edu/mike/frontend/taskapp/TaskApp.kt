package edu.mike.frontend.taskapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class for the TaskApp.
 *
 * This class is annotated with \@HiltAndroidApp to trigger Hilt's code generation,
 * including a base class for the application that serves as the application-level dependency container.
 */
@HiltAndroidApp
class TaskApp : Application()