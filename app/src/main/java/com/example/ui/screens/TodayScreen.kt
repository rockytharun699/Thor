package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.DailyTask
import com.example.ui.components.CircularProgressBadge
import com.example.ui.components.GlowCard
import com.example.ui.components.MohoragaTopBar
import com.example.ui.components.PriorityBadge
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
fun TodayScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val tasks by viewModel.tasks.collectAsState()
    val habits by viewModel.habits.collectAsState()
    val profile by viewModel.userProfile.collectAsState()
    val currentMood by viewModel.todayMood.collectAsState()
    val currentEnergy by viewModel.todayEnergy.collectAsState()

    val isTimerRunning by viewModel.isTimerRunning.collectAsState()
    val timerSecondsLeft by viewModel.timerSecondsLeft.collectAsState()
    val activeTimerTask by viewModel.activeTimerTask.collectAsState()

    var showAddTaskDialog by remember { mutableStateOf(false) }
    var taskToReschedule by remember { mutableStateOf<DailyTask?>(null) }
    var showFocusTimerModal by remember { mutableStateOf(false) }
    var showStartDayModal by remember { mutableStateOf(false) }
    var showWindDownModal by remember { mutableStateOf(false) }

    val completedCount = tasks.count { it.isCompleted }
    val totalCount = tasks.size
    val progressFraction = if (totalCount > 0) completedCount.toFloat() / totalCount.toFloat() else 0f

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                MohoragaTopBar(
                    title = "COMMAND CENTER",
                    subtitle = profile?.name ?: "Mohoraga",
                    actionIcon = Icons.Default.Timer,
                    onActionClick = { showFocusTimerModal = true }
                )
            }

            // Hero Daily Overview Card
            item {
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    GlowCard(
                        borderColor = AmberPrimary.copy(alpha = 0.4f),
                        backgroundColor = DarkSurfaceCard
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "TODAY'S MISSION",
                                        style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.sp),
                                        color = AmberGlow
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Hybrid Recommender & GATE Probability",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = TextPrimary
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Bedtime curfew: ${profile?.targetBedTime ?: "12:30 AM"}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextSecondary
                                    )
                                }

                                CircularProgressBadge(
                                    progress = progressFraction,
                                    text = "${(progressFraction * 100).toInt()}%",
                                    color = AmberPrimary,
                                    size = 60.dp
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Day actions: Start My Day & Wind Down
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Button(
                                    onClick = { showStartDayModal = true },
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("start_my_day_button"),
                                    colors = ButtonDefaults.buttonColors(containerColor = AmberPrimary),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Icon(Icons.Default.WbSunny, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Start Day", style = MaterialTheme.typography.labelMedium)
                                }

                                OutlinedButton(
                                    onClick = { showWindDownModal = true },
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("wind_down_button"),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
                                ) {
                                    Icon(Icons.Default.Nightlight, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Wind Down", style = MaterialTheme.typography.labelMedium)
                                }
                            }
                        }
                    }
                }
            }

            // Energy & Mood Check-In Row
            item {
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    GlowCard(borderColor = DarkBorder) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "ENERGY & READINESS",
                                    style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.sp),
                                    color = TextSecondary
                                )
                                Text(
                                    text = "$currentMood (Lvl $currentEnergy/5)",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = AmberPrimary
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Energy level selector 1..5
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                (1..5).forEach { level ->
                                    val isSelected = (currentEnergy == level)
                                    Box(
                                        modifier = Modifier
                                            .size(44.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(if (isSelected) AmberPrimary else DarkSurfaceVariant)
                                            .border(1.dp, if (isSelected) AmberGlow else DarkBorder, RoundedCornerShape(10.dp))
                                            .clickable { viewModel.updateCheckIn(currentMood, level) }
                                            .testTag("energy_level_$level"),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "$level⚡",
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) Color(0xFF140C00) else TextPrimary,
                                            fontSize = 13.sp
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Mood chips
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                listOf("Focused", "High Drive", "Steady", "Tired").forEach { mood ->
                                    val isSelected = (currentMood == mood)
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isSelected) AmberPrimary.copy(alpha = 0.2f) else DarkSurfaceVariant)
                                            .border(1.dp, if (isSelected) AmberPrimary else DarkBorder, RoundedCornerShape(8.dp))
                                            .clickable { viewModel.updateCheckIn(mood, currentEnergy) }
                                            .padding(horizontal = 10.dp, vertical = 6.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = mood,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = if (isSelected) AmberPrimary else TextSecondary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Timeline Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "DAY TIMELINE",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                            color = TextPrimary
                        )
                        Text(
                            text = "$completedCount of $totalCount completed",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                    }

                    TextButton(
                        onClick = { showAddTaskDialog = true },
                        modifier = Modifier.testTag("quick_add_task_button")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = AmberPrimary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add Task", color = AmberPrimary)
                    }
                }
            }

            // Task List
            items(tasks, key = { it.id }) { task ->
                TaskCard(
                    task = task,
                    onToggle = { viewModel.toggleTask(task) },
                    onReschedule = { taskToReschedule = task },
                    onDelete = { viewModel.deleteTask(task) },
                    onStartFocus = {
                        viewModel.startTimer(task.title, task.durationMinutes.coerceAtMost(60))
                        showFocusTimerModal = true
                    }
                )
            }
        }

        // Quick Add Floating Button
        FloatingActionButton(
            onClick = { showAddTaskDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 90.dp, end = 20.dp)
                .testTag("floating_add_task"),
            containerColor = AmberPrimary,
            contentColor = Color(0xFF140C00)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Task")
        }
    }

    // Add Task Dialog
    if (showAddTaskDialog) {
        AddTaskDialog(
            onDismiss = { showAddTaskDialog = false },
            onConfirm = { title, category, start, end, duration, priority, notes ->
                viewModel.addTask(title, category, start, end, duration, priority, notes)
                showAddTaskDialog = false
            }
        )
    }

    // Reschedule Dialog
    if (taskToReschedule != null) {
        RescheduleDialog(
            task = taskToReschedule!!,
            onDismiss = { taskToReschedule = null },
            onConfirm = { newStart, newEnd ->
                viewModel.rescheduleTask(taskToReschedule!!, newStart, newEnd)
                taskToReschedule = null
            }
        )
    }

    // Focus Timer Dialog
    if (showFocusTimerModal) {
        FocusTimerDialog(
            taskTitle = activeTimerTask,
            secondsLeft = timerSecondsLeft,
            isRunning = isTimerRunning,
            onStart = { viewModel.resumeTimer() },
            onPause = { viewModel.pauseTimer() },
            onReset = { viewModel.resetTimer() },
            onDismiss = { showFocusTimerModal = false }
        )
    }

    // Start Day Guidance Modal
    if (showStartDayModal) {
        AlertDialog(
            onDismissRequest = { showStartDayModal = false },
            containerColor = DarkSurface,
            title = {
                Text("☀️ Morning Ignition", color = AmberPrimary, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("1. Drink 500ml water to kickstart hydration.", color = TextPrimary)
                    Text("2. Take 30-40g high-protein breakfast (eggs + oats).", color = TextPrimary)
                    Text("3. Protect your 9:00 AM Deep Work block: no phone or social media.", color = TextPrimary)
                    Text("First Task: ${tasks.firstOrNull()?.title ?: "Hydration & Setup"}", color = AmberGlow, fontWeight = FontWeight.Medium)
                }
            },
            confirmButton = {
                Button(
                    onClick = { showStartDayModal = false },
                    colors = ButtonDefaults.buttonColors(containerColor = AmberPrimary)
                ) {
                    Text("Let's Execute", color = Color(0xFF140C00))
                }
            }
        )
    }

    // Wind Down Guidance Modal
    if (showWindDownModal) {
        AlertDialog(
            onDismissRequest = { showWindDownModal = false },
            containerColor = DarkSurface,
            title = {
                Text("🌙 Evening Wind Down", color = AmberPrimary, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("1. Completed today: $completedCount of $totalCount tasks.", color = TextPrimary)
                    Text("2. Log your 5-question reflection in the Journal tab.", color = TextPrimary)
                    Text("3. Lower room lights and stop screen usage by 12:00 AM.", color = TextPrimary)
                    Text("Target sleep: ${profile?.targetBedTime ?: "12:30 AM"} strictly.", color = AmberGlow, fontWeight = FontWeight.Medium)
                }
            },
            confirmButton = {
                Button(
                    onClick = { showWindDownModal = false },
                    colors = ButtonDefaults.buttonColors(containerColor = AmberPrimary)
                ) {
                    Text("Complete Review", color = Color(0xFF140C00))
                }
            }
        )
    }
}

@Composable
fun TaskCard(
    task: DailyTask,
    onToggle: () -> Unit,
    onReschedule: () -> Unit,
    onDelete: () -> Unit,
    onStartFocus: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        GlowCard(
            borderColor = if (task.isCompleted) EmeraldSuccess.copy(alpha = 0.3f) else DarkBorder,
            backgroundColor = if (task.isCompleted) Color(0xFF0F181C) else DarkSurfaceCard
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = task.isCompleted,
                        onCheckedChange = { onToggle() },
                        colors = CheckboxDefaults.colors(
                            checkedColor = EmeraldSuccess,
                            uncheckedColor = DarkBorder,
                            checkmarkColor = Color(0xFF0A1014)
                        ),
                        modifier = Modifier.testTag("task_checkbox_${task.id}")
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { expanded = !expanded }
                    ) {
                        Text(
                            text = task.title,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                textDecoration = if (task.isCompleted) androidx.compose.ui.text.style.TextDecoration.LineThrough else null
                            ),
                            color = if (task.isCompleted) TextMuted else TextPrimary
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "${task.startTime} - ${task.endTime} (${task.durationMinutes}m)",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary
                            )
                            PriorityBadge(priority = task.priority)
                        }
                    }

                    IconButton(
                        onClick = onStartFocus,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            Icons.Default.PlayArrow,
                            contentDescription = "Start Focus Sprint",
                            tint = AmberPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                // Expanded Task Details
                AnimatedVisibility(visible = expanded) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, start = 44.dp)
                    ) {
                        if (task.notes.isNotBlank()) {
                            Text(
                                text = "Notes: ${task.notes}",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedButton(
                                onClick = onReschedule,
                                modifier = Modifier.height(32.dp),
                                shape = RoundedCornerShape(6.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = AmberPrimary)
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Reschedule", fontSize = 11.sp)
                            }

                            OutlinedButton(
                                onClick = onDelete,
                                modifier = Modifier.height(32.dp),
                                shape = RoundedCornerShape(6.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFF5252))
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Delete", fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AddTaskDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String, Int, String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Deep work") }
    var startTime by remember { mutableStateOf("04:00 PM") }
    var endTime by remember { mutableStateOf("05:00 PM") }
    var duration by remember { mutableStateOf("60") }
    var priority by remember { mutableStateOf("High") }
    var notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DarkSurface,
        title = { Text("Add Scheduled Task", color = TextPrimary, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Task Title") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = AmberPrimary,
                        unfocusedBorderColor = DarkBorder
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("add_task_title_input")
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = startTime,
                        onValueChange = { startTime = it },
                        label = { Text("Start") },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = AmberPrimary,
                            unfocusedBorderColor = DarkBorder
                        )
                    )
                    OutlinedTextField(
                        value = endTime,
                        onValueChange = { endTime = it },
                        label = { Text("End") },
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
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Action Notes") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = AmberPrimary,
                        unfocusedBorderColor = DarkBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onConfirm(
                            title,
                            category,
                            startTime,
                            endTime,
                            duration.toIntOrNull() ?: 60,
                            priority,
                            notes
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = AmberPrimary),
                modifier = Modifier.testTag("confirm_add_task_button")
            ) {
                Text("Add Task", color = Color(0xFF140C00))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextSecondary)
            }
        }
    )
}

@Composable
fun RescheduleDialog(
    task: DailyTask,
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var newStart by remember { mutableStateOf(task.startTime) }
    var newEnd by remember { mutableStateOf(task.endTime) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DarkSurface,
        title = { Text("Reschedule Task", color = TextPrimary, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(task.title, color = AmberGlow, fontWeight = FontWeight.Medium)
                Text("Shift without guilt into available time buffer:", color = TextSecondary, style = MaterialTheme.typography.bodySmall)

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = newStart,
                        onValueChange = { newStart = it },
                        label = { Text("Start Time") },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = AmberPrimary,
                            unfocusedBorderColor = DarkBorder
                        )
                    )
                    OutlinedTextField(
                        value = newEnd,
                        onValueChange = { newEnd = it },
                        label = { Text("End Time") },
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
                onClick = { onConfirm(newStart, newEnd) },
                colors = ButtonDefaults.buttonColors(containerColor = AmberPrimary)
            ) {
                Text("Update Schedule", color = Color(0xFF140C00))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel", color = TextSecondary) }
        }
    )
}

@Composable
fun FocusTimerDialog(
    taskTitle: String,
    secondsLeft: Int,
    isRunning: Boolean,
    onStart: () -> Unit,
    onPause: () -> Unit,
    onReset: () -> Unit,
    onDismiss: () -> Unit
) {
    val minutes = secondsLeft / 60
    val seconds = secondsLeft % 60
    val timeFormatted = String.format("%02d:%02d", minutes, seconds)

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DarkSurface,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("⚡ Focus Sprint", color = AmberPrimary, fontWeight = FontWeight.Bold)
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                }
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = taskTitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(20.dp))

                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF141926))
                        .border(3.dp, if (isRunning) AmberPrimary else DarkBorder, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = timeFormatted,
                            style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )
                        Text(
                            text = if (isRunning) "IN PROGRESS" else "PAUSED",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isRunning) EmeraldSuccess else TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Button(
                        onClick = { if (isRunning) onPause() else onStart() },
                        colors = ButtonDefaults.buttonColors(containerColor = AmberPrimary),
                        modifier = Modifier.testTag("focus_timer_toggle")
                    ) {
                        Text(if (isRunning) "Pause" else "Start Sprint", color = Color(0xFF140C00))
                    }

                    OutlinedButton(
                        onClick = onReset,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                        border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
                    ) {
                        Text("Reset (25m)")
                    }
                }
            }
        },
        confirmButton = {}
    )
}
