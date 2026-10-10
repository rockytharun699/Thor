package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.example.data.local.Habit
import com.example.ui.components.GlowCard
import com.example.ui.components.MohoragaTopBar
import com.example.ui.theme.AmberGlow
import com.example.ui.theme.AmberPrimary
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.MainViewModel

@Composable
fun HabitsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val habits by viewModel.habits.collectAsState()
    val habitLogs by viewModel.habitLogs.collectAsState()

    var showAddHabitDialog by remember { mutableStateOf(false) }

    val completedHabitsCount = habits.count { habit -> habitLogs.any { it.habitId == habit.id } }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                MohoragaTopBar(
                    title = "HABIT PROTOCOL",
                    subtitle = "$completedHabitsCount / ${habits.size} Habits Completed Today"
                )
            }

            // Streak Motivation Card
            item {
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    GlowCard(
                        borderColor = AmberPrimary.copy(alpha = 0.4f),
                        backgroundColor = DarkSurfaceCard
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "DISCIPLINE & COMPOUND MOMENTUM",
                                    style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.sp),
                                    color = AmberGlow
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Consistency Over Streaks",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Missing one day is an accident; missing two days is the start of a new bad habit. Planned rest days do not break your identity.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF331D08)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.LocalFireDepartment,
                                    contentDescription = "Fire",
                                    tint = AmberPrimary,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Habit List Header
            item {
                Text(
                    text = "ACTIVE PROTOCOLS",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                    color = TextPrimary,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 2.dp)
                )
            }

            items(habits, key = { it.id }) { habit ->
                val isDoneToday = habitLogs.any { it.habitId == habit.id }
                HabitCard(
                    habit = habit,
                    isDoneToday = isDoneToday,
                    onToggle = { viewModel.toggleHabitToday(habit) }
                )
            }
        }

        FloatingActionButton(
            onClick = { showAddHabitDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 90.dp, end = 20.dp)
                .testTag("floating_add_habit"),
            containerColor = AmberPrimary,
            contentColor = Color(0xFF140C00)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Habit")
        }
    }

    if (showAddHabitDialog) {
        AddHabitDialog(
            onDismiss = { showAddHabitDialog = false },
            onConfirm = { title, category, frequency, target, reminder ->
                viewModel.addHabit(title, category, frequency, target, reminder)
                showAddHabitDialog = false
            }
        )
    }
}

@Composable
fun HabitCard(
    habit: Habit,
    isDoneToday: Boolean,
    onToggle: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 3.dp)
    ) {
        GlowCard(
            borderColor = if (isDoneToday) EmeraldSuccess.copy(alpha = 0.3f) else DarkBorder,
            backgroundColor = if (isDoneToday) Color(0xFF0F181B) else DarkSurfaceCard
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isDoneToday,
                        onCheckedChange = { onToggle() },
                        colors = CheckboxDefaults.colors(
                            checkedColor = EmeraldSuccess,
                            uncheckedColor = DarkBorder
                        ),
                        modifier = Modifier.testTag("habit_checkbox_${habit.id}")
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Column {
                        Text(
                            text = habit.title,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                textDecoration = if (isDoneToday) androidx.compose.ui.text.style.TextDecoration.LineThrough else null
                            ),
                            color = if (isDoneToday) TextMuted else TextPrimary
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = "${habit.category} • Target ${habit.targetPerWeek}x/week",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                    }
                }

                // Streak Badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        Icons.Default.LocalFireDepartment,
                        contentDescription = "Streak",
                        tint = if (habit.currentStreak > 0) AmberPrimary else TextMuted,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "${habit.currentStreak}d",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = if (habit.currentStreak > 0) AmberPrimary else TextMuted
                    )
                }
            }
        }
    }
}

@Composable
fun AddHabitDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, Int, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Productivity") }
    var frequency by remember { mutableStateOf("DAILY") }
    var targetPerWeek by remember { mutableStateOf("7") }
    var reminderTime by remember { mutableStateOf("09:00 AM") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DarkSurface,
        title = { Text("Add Habit Protocol", color = AmberPrimary, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Habit Name") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = AmberPrimary,
                        unfocusedBorderColor = DarkBorder
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("add_habit_title_input")
                )

                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Category") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = AmberPrimary,
                        unfocusedBorderColor = DarkBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = targetPerWeek,
                        onValueChange = { targetPerWeek = it },
                        label = { Text("Days / Week") },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = AmberPrimary,
                            unfocusedBorderColor = DarkBorder
                        )
                    )
                    OutlinedTextField(
                        value = reminderTime,
                        onValueChange = { reminderTime = it },
                        label = { Text("Reminder") },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = AmberPrimary,
                            unfocusedBorderColor = DarkBorder
                        )
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onConfirm(title, category, frequency, targetPerWeek.toIntOrNull() ?: 7, reminderTime)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = AmberPrimary),
                modifier = Modifier.testTag("confirm_add_habit")
            ) {
                Text("Save Habit", color = Color(0xFF140C00))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel", color = TextSecondary) }
        }
    )
}
