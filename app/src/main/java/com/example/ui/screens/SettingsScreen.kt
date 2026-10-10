package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.UserProfile
import com.example.ui.components.GlowCard
import com.example.ui.components.MohoragaTopBar
import com.example.ui.theme.AmberGlow
import com.example.ui.theme.AmberPrimary
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.MainViewModel

@Composable
fun SettingsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val profileState by viewModel.userProfile.collectAsState()

    var name by remember(profileState) { mutableStateOf(profileState?.name ?: "Mohoraga") }
    var age by remember(profileState) { mutableStateOf(profileState?.age?.toString() ?: "21") }
    var education by remember(profileState) { mutableStateOf(profileState?.education ?: "Final-year B.Tech student") }
    var branch by remember(profileState) { mutableStateOf(profileState?.branch ?: "CSE (AI & ML)") }
    var careerGoal by remember(profileState) { mutableStateOf(profileState?.primaryCareerGoal ?: "Become an AI/ML Engineer") }
    var examGoal by remember(profileState) { mutableStateOf(profileState?.examGoal ?: "GATE DA Preparation") }
    var communicationGoal by remember(profileState) { mutableStateOf(profileState?.communicationGoal ?: "Improve English fluency & interview answers") }
    var currentWeight by remember(profileState) { mutableStateOf(profileState?.currentWeightKg?.toString() ?: "65") }
    var targetWeight by remember(profileState) { mutableStateOf(profileState?.targetWeightKg?.toString() ?: "72") }
    var wakeTime by remember(profileState) { mutableStateOf(profileState?.targetWakeTime ?: "08:00 AM") }
    var bedTime by remember(profileState) { mutableStateOf(profileState?.targetBedTime ?: "12:30 AM") }
    var calorieTarget by remember(profileState) { mutableStateOf(profileState?.dailyTargetCalorie?.toString() ?: "2500") }
    var proteinTarget by remember(profileState) { mutableStateOf(profileState?.dailyTargetProteinGrams?.toString() ?: "135") }

    var showExportDialog by remember { mutableStateOf(false) }
    var exportedJsonText by remember { mutableStateOf("") }
    var showResetConfirmDialog by remember { mutableStateOf(false) }
    var savedSuccessSnackbar by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            MohoragaTopBar(
                title = "SETTINGS & PROFILE",
                subtitle = "Personal Preferences & System Controls"
            )
        }

        // Profile Form Card
        item {
            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                GlowCard(borderColor = DarkBorder) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "PERSONAL PROFILE & CAREER",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                            color = AmberPrimary
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = name,
                                onValueChange = { name = it },
                                label = { Text("Name") },
                                modifier = Modifier.weight(2f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary,
                                    focusedBorderColor = AmberPrimary,
                                    unfocusedBorderColor = DarkBorder
                                )
                            )
                            OutlinedTextField(
                                value = age,
                                onValueChange = { age = it },
                                label = { Text("Age") },
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary,
                                    focusedBorderColor = AmberPrimary,
                                    unfocusedBorderColor = DarkBorder
                                )
                            )
                        }

                        OutlinedTextField(
                            value = branch,
                            onValueChange = { branch = it },
                            label = { Text("Degree & Branch") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                focusedBorderColor = AmberPrimary,
                                unfocusedBorderColor = DarkBorder
                            )
                        )

                        OutlinedTextField(
                            value = careerGoal,
                            onValueChange = { careerGoal = it },
                            label = { Text("Primary Career Objective") },
                            modifier = Modifier.fillMaxWidth().testTag("settings_career_goal_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                focusedBorderColor = AmberPrimary,
                                unfocusedBorderColor = DarkBorder
                            )
                        )

                        OutlinedTextField(
                            value = examGoal,
                            onValueChange = { examGoal = it },
                            label = { Text("Exam Objective") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                focusedBorderColor = AmberPrimary,
                                unfocusedBorderColor = DarkBorder
                            )
                        )

                        OutlinedTextField(
                            value = communicationGoal,
                            onValueChange = { communicationGoal = it },
                            label = { Text("Communication Goal") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                focusedBorderColor = AmberPrimary,
                                unfocusedBorderColor = DarkBorder
                            )
                        )
                    }
                }
            }
        }

        // Schedule & Fitness Card
        item {
            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                GlowCard(borderColor = DarkBorder) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "SCHEDULE & BODY TARGETS",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                            color = AmberGlow
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = wakeTime,
                                onValueChange = { wakeTime = it },
                                label = { Text("Target Wake Time") },
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary,
                                    focusedBorderColor = AmberPrimary,
                                    unfocusedBorderColor = DarkBorder
                                )
                            )
                            OutlinedTextField(
                                value = bedTime,
                                onValueChange = { bedTime = it },
                                label = { Text("Target Bedtime") },
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary,
                                    focusedBorderColor = AmberPrimary,
                                    unfocusedBorderColor = DarkBorder
                                )
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = currentWeight,
                                onValueChange = { currentWeight = it },
                                label = { Text("Current Weight (kg)") },
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary,
                                    focusedBorderColor = AmberPrimary,
                                    unfocusedBorderColor = DarkBorder
                                )
                            )
                            OutlinedTextField(
                                value = targetWeight,
                                onValueChange = { targetWeight = it },
                                label = { Text("Target Weight (kg)") },
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary,
                                    focusedBorderColor = AmberPrimary,
                                    unfocusedBorderColor = DarkBorder
                                )
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = calorieTarget,
                                onValueChange = { calorieTarget = it },
                                label = { Text("Daily Calories (kcal)") },
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary,
                                    focusedBorderColor = AmberPrimary,
                                    unfocusedBorderColor = DarkBorder
                                )
                            )
                            OutlinedTextField(
                                value = proteinTarget,
                                onValueChange = { proteinTarget = it },
                                label = { Text("Daily Protein (g)") },
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary,
                                    focusedBorderColor = AmberPrimary,
                                    unfocusedBorderColor = DarkBorder
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Button(
                            onClick = {
                                val current = profileState ?: UserProfile()
                                val updated = current.copy(
                                    name = name,
                                    age = age.toIntOrNull() ?: 21,
                                    branch = branch,
                                    primaryCareerGoal = careerGoal,
                                    examGoal = examGoal,
                                    communicationGoal = communicationGoal,
                                    targetWakeTime = wakeTime,
                                    targetBedTime = bedTime,
                                    currentWeightKg = currentWeight.toFloatOrNull() ?: 65f,
                                    targetWeightKg = targetWeight.toFloatOrNull() ?: 72f,
                                    dailyTargetCalorie = calorieTarget.toIntOrNull() ?: 2500,
                                    dailyTargetProteinGrams = proteinTarget.toIntOrNull() ?: 135
                                )
                                viewModel.updateProfile(updated)
                                savedSuccessSnackbar = true
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = AmberPrimary),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("save_profile_button"),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Save, contentDescription = null, tint = Color(0xFF140C00))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Save Profile Changes", color = Color(0xFF140C00), fontWeight = FontWeight.Bold)
                        }

                        if (savedSuccessSnackbar) {
                            Text(
                                text = "✓ Profile updated successfully!",
                                color = Color(0xFF00E676),
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Data Management Card
        item {
            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                GlowCard(borderColor = DarkBorder) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "DATA STORAGE & PORTABILITY",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                            color = TextPrimary
                        )

                        Text(
                            text = "All your data is saved in encrypted local Room SQLite database on your device. Cross-session persistence is fully active.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = {
                                    viewModel.exportDataJson { json ->
                                        exportedJsonText = json
                                        showExportDialog = true
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceVariant),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("export_data_button")
                            ) {
                                Icon(Icons.Default.FileDownload, contentDescription = null, tint = AmberPrimary, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Export JSON", color = TextPrimary, fontSize = 12.sp)
                            }

                            OutlinedButton(
                                onClick = { showResetConfirmDialog = true },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFF5252)),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFF5252).copy(alpha = 0.5f)),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("reset_data_button")
                            ) {
                                Icon(Icons.Default.RestartAlt, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Reset All Data", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }

    // Export Dialog
    if (showExportDialog) {
        AlertDialog(
            onDismissRequest = { showExportDialog = false },
            containerColor = DarkSurface,
            title = { Text("Exported Personal Data", color = AmberPrimary, fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(
                    value = exportedJsonText,
                    onValueChange = {},
                    readOnly = true,
                    minLines = 8,
                    maxLines = 12,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = AmberPrimary,
                        unfocusedBorderColor = DarkBorder
                    )
                )
            },
            confirmButton = {
                Button(
                    onClick = { showExportDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = AmberPrimary)
                ) {
                    Text("Done", color = Color(0xFF140C00))
                }
            }
        )
    }

    // Reset Confirm Dialog
    if (showResetConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showResetConfirmDialog = false },
            containerColor = DarkSurface,
            title = { Text("Confirm Complete Data Reset", color = Color(0xFFFF5252), fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    text = "Are you sure you want to reset all tasks, milestones, habits, and journals back to clean initial baseline? This action cannot be undone.",
                    color = TextPrimary,
                    style = MaterialTheme.typography.bodySmall
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.resetData()
                        showResetConfirmDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5252))
                ) {
                    Text("Yes, Reset Everything", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetConfirmDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            }
        )
    }
}
