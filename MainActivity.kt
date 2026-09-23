package com.personalfitnessos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.personalfitnessos.core.di.ViewModelFactory
import com.personalfitnessos.ui.navigation.AppNavGraph
import com.personalfitnessos.ui.root.RootViewModel
import com.personalfitnessos.ui.theme.PersonalFitnessTheme

/**
 * Single-activity architecture (spec §1): this Activity hosts Compose Navigation and never
 * gains a second Activity. Screen-to-screen flow is entirely NavHost destinations.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as PersonalFitnessApp
        val viewModelFactory = ViewModelFactory(app.repositories)

        setContent {
            PersonalFitnessTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val rootViewModel: RootViewModel = viewModel(factory = viewModelFactory)
                    val onboardingCompleted by rootViewModel.onboardingCompleted.collectAsState()

                    when (onboardingCompleted) {
                        null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                        else -> AppNavGraph(
                            viewModelFactory = viewModelFactory,
                            startAtOnboarding = onboardingCompleted == false,
                        )
                    }
                }
            }
        }
    }
}
