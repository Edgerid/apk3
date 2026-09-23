package com.personalfitnessos.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.ui.graphics.vector.ImageVector

/** The five bottom-nav destinations from spec §4. */
enum class TopLevelDestination(val route: String, val label: String, val icon: ImageVector) {
    HOME("home", "Home", Icons.Filled.Home),
    NUTRITION("nutrition", "Nutrition", Icons.Filled.Restaurant),
    WORKOUT("workout", "Workout", Icons.Filled.FitnessCenter),
    PROGRESS("progress", "Progress", Icons.Filled.ShowChart),
    MORE("more", "More", Icons.Filled.MoreHoriz),
}
