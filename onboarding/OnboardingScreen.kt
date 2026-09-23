package com.personalfitnessos.ui.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.personalfitnessos.data.local.entity.ActivityLevel
import com.personalfitnessos.data.local.entity.CalorieTargetMethod
import com.personalfitnessos.data.local.entity.GoalDirection
import com.personalfitnessos.data.local.entity.Sex

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel,
    onFinished: () -> Unit,
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.saved) {
        if (state.saved) onFinished()
    }

    Scaffold(topBar = { TopAppBar(title = { Text("Welcome") }) }) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text("Let's set up your profile. Everything here can be changed later, and none of it is required except what you need for a calorie estimate.")

            OutlinedTextField(
                value = state.nickname,
                onValueChange = viewModel::updateNickname,
                label = { Text("Nickname (optional)") },
                modifier = Modifier.fillMaxWidth(),
            )

            OutlinedTextField(
                value = state.ageText,
                onValueChange = viewModel::updateAge,
                label = { Text("Age (optional)") },
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
            )

            Text("Sex (optional, only used to estimate calories)")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(Sex.MALE, Sex.FEMALE, Sex.UNSPECIFIED).forEach { option ->
                    FilterChip(
                        selected = state.sex == option,
                        onClick = { viewModel.updateSex(option) },
                        label = { Text(option.name.lowercase().replaceFirstChar { it.uppercase() }) },
                    )
                }
            }

            OutlinedTextField(
                value = state.heightCmText,
                onValueChange = viewModel::updateHeightCm,
                label = { Text("Height (cm)") },
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
            )

            OutlinedTextField(
                value = state.currentWeightKgText,
                onValueChange = viewModel::updateCurrentWeightKg,
                label = { Text("Current weight (kg)") },
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
            )

            OutlinedTextField(
                value = state.targetWeightKgText,
                onValueChange = viewModel::updateTargetWeightKg,
                label = { Text("Target weight (kg, optional)") },
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
            )

            Text("Primary goal")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                GoalDirection.entries.forEach { option ->
                    FilterChip(
                        selected = state.goalDirection == option,
                        onClick = { viewModel.updateGoalDirection(option) },
                        label = { Text(option.name.lowercase().replaceFirstChar { it.uppercase() }) },
                    )
                }
            }

            Text("Activity level")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ActivityLevel.entries.forEach { option ->
                    FilterChip(
                        selected = state.activityLevel == option,
                        onClick = { viewModel.updateActivityLevel(option) },
                        label = { Text(option.name.take(3)) },
                    )
                }
            }

            Text("How should your calorie target be set?")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CalorieTargetMethod.entries.forEach { option ->
                    FilterChip(
                        selected = state.calorieTargetMethod == option,
                        onClick = { viewModel.updateCalorieMethod(option) },
                        label = { Text(option.name.replace('_', ' ')) },
                    )
                }
            }

            if (state.calorieTargetMethod != CalorieTargetMethod.TDEE_ESTIMATE) {
                OutlinedTextField(
                    value = state.fixedOrManualCalorieText,
                    onValueChange = viewModel::updateFixedOrManualCalorie,
                    label = { Text("Daily calorie target") },
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                )
            } else {
                val preview = viewModel.previewBreakdown()
                if (preview != null) {
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("Estimate (not medically exact)")
                            Text("BMR: ${preview.bmr} kcal")
                            Text("Activity estimate (TDEE): ${preview.tdee} kcal")
                            Text("Goal adjustment: ${if (preview.goalAdjustmentKcal >= 0) "+" else ""}${preview.goalAdjustmentKcal} kcal")
                            Text("Final target: ${preview.finalTargetKcal} kcal / day")
                        }
                    }
                }
            }

            OutlinedTextField(
                value = state.gymTargetSessionsPerWeekText,
                onValueChange = viewModel::updateGymTargetSessions,
                label = { Text("Gym sessions per week (target)") },
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
            )

            state.errorMessage?.let { Text(it) }

            Button(
                onClick = viewModel::save,
                enabled = !state.isSaving,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(if (state.isSaving) "Saving..." else "Get started")
            }
        }
    }
}
