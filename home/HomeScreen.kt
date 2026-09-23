package com.personalfitnessos.ui.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.personalfitnessos.ui.components.EmptyState

/**
 * Phase 8 (dashboard/analytics) fills this in with today's calories/protein, workout,
 * attendance streak, goals, weight trend, and quick actions per spec §25. Placeholder for
 * now so navigation and the app shell are real and launchable before that phase lands.
 */
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(title = { Text("Personal Fitness OS") }) },
    ) { padding ->
        EmptyState(
            title = "Dashboard coming together",
            message = "Today's calories, workout, and streaks will show up here once " +
                "onboarding and logging are wired up.",
            modifier = Modifier.padding(padding),
        )
    }
}
