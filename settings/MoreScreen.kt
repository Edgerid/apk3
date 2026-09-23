package com.personalfitnessos.ui.settings

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.personalfitnessos.ui.components.EmptyState

/** Goals, attendance history, body tracking, Health Connect, export/import, settings. */
@Composable
fun MoreScreen(modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(title = { Text("More") }) },
    ) { padding ->
        EmptyState(
            title = "More",
            message = "Goals, attendance, body tracking, and settings will live here.",
            modifier = Modifier.padding(padding),
        )
    }
}
