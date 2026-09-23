package com.personalfitnessos.ui.workout

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.personalfitnessos.ui.components.EmptyState

@Composable
fun WorkoutScreen(modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(title = { Text("Workout") }) },
    ) { padding ->
        EmptyState(
            title = "No workouts yet",
            message = "Start your first workout to begin tracking your progress.",
            modifier = Modifier.padding(padding),
        )
    }
}
