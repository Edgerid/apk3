package com.personalfitnessos.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.personalfitnessos.data.repository.RepositoryProvider
import com.personalfitnessos.core.time.SystemAppClock
import com.personalfitnessos.ui.onboarding.OnboardingViewModel
import com.personalfitnessos.ui.root.RootViewModel

/**
 * One factory for every ViewModel in the app. Each ViewModel constructor takes exactly the
 * repositories it needs from [RepositoryProvider] — this factory just forwards them. Keeps
 * DI visible and debuggable without Hilt/Dagger (spec §2), at the cost of editing this
 * `when` block whenever a new ViewModel is added.
 */
class ViewModelFactory(private val repositories: RepositoryProvider) : ViewModelProvider.Factory {
    private val clock = SystemAppClock()

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return when (modelClass) {
            RootViewModel::class.java ->
                RootViewModel(repositories.userProfile)

            OnboardingViewModel::class.java ->
                OnboardingViewModel(repositories.userProfile, repositories.nutrition, clock)

            else -> throw IllegalArgumentException(
                "No creator registered for ${modelClass.name}. Add it to ViewModelFactory."
            )
        } as T
    }
}

