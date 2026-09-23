package com.personalfitnessos.ui.nutrition

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.personalfitnessos.ui.components.EmptyState

@Composable
fun NutritionScreen(modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(title = { Text("Nutrition") }) },
    ) { padding ->
        EmptyState(
            title = "No food logged today",
            message = "Search for a food, scan a barcode, or add a quick calorie entry to get started.",
            modifier = Modifier.padding(padding),
        )
    }
}
