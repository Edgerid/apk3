package com.personalfitnessos.ui.root

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personalfitnessos.data.repository.UserProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/** null = still loading the profile row; otherwise whether onboarding has been completed. */
class RootViewModel(userProfileRepository: UserProfileRepository) : ViewModel() {
    private val _onboardingCompleted = MutableStateFlow<Boolean?>(null)
    val onboardingCompleted: StateFlow<Boolean?> = _onboardingCompleted.asStateFlow()

    init {
        userProfileRepository.observeProfile()
            .onEach { profile -> _onboardingCompleted.value = profile?.onboardingCompleted ?: false }
            .launchIn(viewModelScope)
    }
}
