package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import com.example.data.local.Goal
import com.example.data.local.GoalMilestone
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
fun GoalsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val goals by viewModel.goals.collectAsState()
    val milestones by viewModel.milestones.collectAsState()

    var selectedCategory by remember { mutableStateOf("All") }
    var showAddGoalDialog by remember { mutableStateOf(false) }
    var goalForNewMilestone by remember { mutableStateOf<Goal?>(null) }

    val categories = listOf("All", "Career", "Projects", "GATE", "Communication", "Fitness", "Personal")

    val filteredGoals = if (selectedCategory == "All") {
        goals
    } else {
        goals.filter { it.category.equals(selectedCategory, ignoreCase = true) }
    }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                MohoragaTopBar(
                    title = "GOALS & MILESTONES",
                    subtitle = "${goals.size} Active Objectives"
                )
            }

            // Category Chips Row
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categories.forEach { cat ->
                        val isSelected = (selectedCategory == cat)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) AmberPrimary else DarkSurfaceVariant)
                                .border(1.dp, if (isSelected) AmberGlow else DarkBorder, RoundedCornerShape(10.dp))
                                .clickable { selectedCategory = cat }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = cat,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = if (isSelected) Color(0xFF140C00) else TextPrimary
                            )
                        }
                    }
                }
            }

            // Goals list
            items(filteredGoals, key = { it.id }) { goal ->
                val goalMilestones = milestones.filter { it.goalId == goal.id }
                GoalCard(
                    goal = goal,
                    milestones = goalMilestones,
                    onToggleMilestone = { milestone -> viewModel.toggleMilestone(milestone) },
                    onAddMilestoneClick = { goalForNewMilestone = goal }
                )
            }
        }

        // Add Goal Floating Action Button
        FloatingActionButton(
            onClick = { showAddGoalDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 90.dp, end = 20.dp)
                .testTag("floating_add_goal"),
            containerColor = AmberPrimary,
            contentColor = Color(0xFF140C00)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Goal")
        }
    }

    // Add Goal Dialog
    if (showAddGoalDialog) {
        AddGoalDialog(
            onDismiss = { showAddGoalDialog = false },
            onConfirm = { title, desc, reason, cat, target, priority, criteria ->
                viewModel.addGoal(title, desc, reason, cat, target, priority, criteria)
                showAddGoalDialog = false
            }
        )
    }

    // Add Milestone Dialog
    if (goalForNewMilestone != null) {
        AddMilestoneDialog(
            goalTitle = goalForNewMilestone!!.title,
            onDismiss = { goalForNewMilestone = null },
            onConfirm = { title, targetDate ->
                viewModel.addMilestone(goalForNewMilestone!!.id, title, targetDate)
                goalForNewMilestone = null
            }
        )
    }
}

@Composable
fun GoalCard(
    goal: Goal,
    milestones: List<GoalMilestone>,
    onToggleMilestone: (GoalMilestone) -> Unit,
    onAddMilestoneClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        GlowCard(
            borderColor = DarkBorder,
            backgroundColor = DarkSurfaceCard
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(AmberPrimary.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Flag, contentDescription = null, tint = AmberPrimary, modifier = Modifier.size(20.dp))
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = goal.title,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = TextPrimary
                            )
                            Text(
                                text = "${goal.category} • Target: ${goal.targetDate}",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        PriorityBadge(priority = goal.priority)
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = "Expand",
                            tint = TextSecondary,
                            modifier = Modifier.clickable { expanded = !expanded }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Progress Bar
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Progress",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                        Text(
                            text = "${goal.progressPercentage}%",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = AmberPrimary
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { goal.progressPercentage / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = AmberPrimary,
                        trackColor = DarkBorder
                    )
                }

                AnimatedVisibility(visible = expanded) {
                    Column(modifier = Modifier.padding(top = 12.dp)) {
                        Text(
                            text = goal.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextPrimary
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Why it matters: ${goal.reason}",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Success criteria: ${goal.successCriteria}",
                            style = MaterialTheme.typography.labelSmall,
                            color = AmberGlow
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Milestones Section
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "MILESTONES (${milestones.count { it.isCompleted }}/${milestones.size})",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                                color = TextPrimary
                            )

                            TextButton(onClick = onAddMilestoneClick) {
                                Text("+ Add Milestone", style = MaterialTheme.typography.labelSmall, color = AmberPrimary)
                            }
                        }

                        milestones.forEach { milestone ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = milestone.isCompleted,
                                    onCheckedChange = { onToggleMilestone(milestone) },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = EmeraldSuccess,
                                        uncheckedColor = DarkBorder
                                    ),
                                    modifier = Modifier.testTag("milestone_checkbox_${milestone.id}")
                                )
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = milestone.title,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            textDecoration = if (milestone.isCompleted) androidx.compose.ui.text.style.TextDecoration.LineThrough else null
                                        ),
                                        color = if (milestone.isCompleted) TextMuted else TextPrimary
                                    )
                                    if (milestone.targetDate.isNotBlank()) {
                                        Text(
                                            text = "Due: ${milestone.targetDate}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = TextSecondary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AddGoalDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String, String, String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var reason by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Career") }
    var targetDate by remember { mutableStateOf("2027-05-01") }
    var priority by remember { mutableStateOf("High") }
    var successCriteria by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DarkSurface,
        title = { Text("Create Strategic Goal", color = TextPrimary, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Goal Title") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = AmberPrimary,
                        unfocusedBorderColor = DarkBorder
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("add_goal_title_input")
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = AmberPrimary,
                        unfocusedBorderColor = DarkBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = reason,
                    onValueChange = { reason = it },
                    label = { Text("Why It Matters") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = AmberPrimary,
                        unfocusedBorderColor = DarkBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = successCriteria,
                    onValueChange = { successCriteria = it },
                    label = { Text("Measurable Success Criteria") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = AmberPrimary,
                        unfocusedBorderColor = DarkBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = targetDate,
                    onValueChange = { targetDate = it },
                    label = { Text("Target Date (YYYY-MM-DD)") },
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
                        onConfirm(title, description, reason, category, targetDate, priority, successCriteria)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = AmberPrimary),
                modifier = Modifier.testTag("confirm_add_goal_button")
            ) {
                Text("Save Goal", color = Color(0xFF140C00))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel", color = TextSecondary) }
        }
    )
}

@Composable
fun AddMilestoneDialog(
    goalTitle: String,
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var targetDate by remember { mutableStateOf("2026-12-01") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DarkSurface,
        title = { Text("Add Milestone", color = TextPrimary, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("For: $goalTitle", color = AmberGlow, style = MaterialTheme.typography.bodySmall)

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Milestone Action") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = AmberPrimary,
                        unfocusedBorderColor = DarkBorder
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("add_milestone_title_input")
                )

                OutlinedTextField(
                    value = targetDate,
                    onValueChange = { targetDate = it },
                    label = { Text("Due Date (YYYY-MM-DD)") },
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
                        onConfirm(title, targetDate)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = AmberPrimary)
            ) {
                Text("Add Milestone", color = Color(0xFF140C00))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel", color = TextSecondary) }
        }
    )
}
