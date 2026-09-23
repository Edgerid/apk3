package com.personalfitnessos.ui.progress

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.personalfitnessos.ui.components.EmptyState

@Composable
fun ProgressScreen(modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(title = { Text("Progress") }) },
    ) { padding ->
        EmptyState(
            title = "Not enough data yet",
            message = "Log a few days of weight, meals, and workouts to see trends here.",
            modifier = Modifier.padding(padding),
        )
    }
}
