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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.data.local.NutritionLog
import com.example.data.local.WorkoutLog
import com.example.ui.components.CircularProgressBadge
import com.example.ui.components.GlowCard
import com.example.ui.components.MohoragaTopBar
import com.example.ui.theme.AmberGlow
import com.example.ui.theme.AmberPrimary
import com.example.ui.theme.CyberCyan
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
fun FitnessScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Workout, 1: Nutrition & Water

    val workouts by viewModel.workouts.collectAsState()
    val nutritionLogs by viewModel.nutritionLogs.collectAsState()
    val waterLog by viewModel.waterLog.collectAsState()
    val profile by viewModel.userProfile.collectAsState()

    var showLogWorkoutDialog by remember { mutableStateOf(false) }
    var showCustomMealDialog by remember { mutableStateOf(false) }

    val totalCalories = nutritionLogs.sumOf { it.calories }
    val totalProtein = nutritionLogs.sumOf { it.proteinGrams.toDouble() }.toFloat()
    val targetCalories = profile?.dailyTargetCalorie ?: 2500
    val targetProtein = profile?.dailyTargetProteinGrams ?: 135

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                MohoragaTopBar(
                    title = "FITNESS & HYPERTROPHY",
                    subtitle = "Target: 65kg ➔ 72kg Lean Mass"
                )
            }

            // Tab Switcher
            item {
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    TabRow(
                        selectedTabIndex = selectedTab,
                        containerColor = DarkSurfaceCard,
                        contentColor = AmberPrimary,
                        indicator = { tabPositions ->
                            TabRowDefaults.SecondaryIndicator(
                                Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                                color = AmberPrimary
                            )
                        },
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.dp, DarkBorder, RoundedCornerShape(12.dp))
                    ) {
                        Tab(
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            text = { Text("Hypertrophy Training", fontWeight = FontWeight.SemiBold, fontSize = 12.sp) }
                        )
                        Tab(
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            text = { Text("Nutrition & Fuel", fontWeight = FontWeight.SemiBold, fontSize = 12.sp) }
                        )
                    }
                }
            }

            if (selectedTab == 0) {
                // Workout Sub-Screen
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
                                    Column {
                                        Text(
                                            text = "BEGINNER HYPERTROPHY SPLIT",
                                            style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.sp),
                                            color = AmberGlow
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = "4-Day Upper / Lower Progression",
                                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                            color = TextPrimary
                                        )
                                    }

                                    Button(
                                        onClick = { showLogWorkoutDialog = true },
                                        colors = ButtonDefaults.buttonColors(containerColor = AmberPrimary),
                                        modifier = Modifier.testTag("log_workout_button"),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("+ Log Session", color = Color(0xFF140C00), fontSize = 11.sp)
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = "Current Focus: Progressive overload on multi-joint compounds (Bench press, Lat pulldown, Squats, Overhead press). 3-4 sets of 8-12 reps with 90s rest intervals.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                            }
                        }
                    }
                }

                // Workout Logs History
                item {
                    Text(
                        text = "RECENT WORKOUT SESSIONS",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                        color = TextPrimary,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 2.dp)
                    )
                }

                if (workouts.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        ) {
                            GlowCard(borderColor = DarkBorder) {
                                Text(
                                    text = "No workouts logged yet. Tap '+ Log Session' after today's gym training.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                            }
                        }
                    }
                } else {
                    items(workouts, key = { it.id }) { workout ->
                        WorkoutCard(workout = workout)
                    }
                }
            } else {
                // Nutrition & Water Sub-Screen
                item {
                    Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                        GlowCard(
                            borderColor = CyberCyan.copy(alpha = 0.4f),
                            backgroundColor = DarkSurfaceCard
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "DAILY NUTRITION TARGETS",
                                            style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.sp),
                                            color = CyberCyan
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = "$totalCalories / $targetCalories kcal",
                                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                            color = TextPrimary
                                        )
                                    }

                                    CircularProgressBadge(
                                        progress = (totalCalories.toFloat() / targetCalories.toFloat()).coerceIn(0f, 1f),
                                        text = "${((totalCalories.toFloat() / targetCalories.toFloat()) * 100).toInt()}%",
                                        color = CyberCyan
                                    )
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                // Protein progress bar
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "Protein (${totalProtein.toInt()}g / ${targetProtein}g)",
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            color = TextPrimary
                                        )
                                        Text(
                                            text = "${((totalProtein / targetProtein) * 100).toInt()}% Target",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = AmberGlow
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    LinearProgressIndicator(
                                        progress = { (totalProtein / targetProtein).coerceIn(0f, 1f) },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(8.dp)
                                            .clip(RoundedCornerShape(4.dp)),
                                        color = AmberPrimary,
                                        trackColor = DarkBorder
                                    )
                                }
                            }
                        }
                    }
                }

                // Water Hydration Tracker
                item {
                    val glasses = waterLog?.glassesCount ?: 0
                    Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                        GlowCard(borderColor = DarkBorder) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.LocalDrink, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(28.dp))
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = "Hydration: $glasses / 8 Glasses",
                                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                            color = TextPrimary
                                        )
                                        Text(
                                            text = "${glasses * 300}ml / 2400ml consumed",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = TextSecondary
                                        )
                                    }
                                }

                                Button(
                                    onClick = { viewModel.incrementWater() },
                                    colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                                    modifier = Modifier.testTag("increment_water_button"),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("+1 Glass", color = Color(0xFF001F24), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                // Quick-Add Indian High-Protein Presets
                item {
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "QUICK-ADD HIGH-PROTEIN FOODS",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                                color = TextPrimary
                            )
                            TextButton(onClick = { showCustomMealDialog = true }) {
                                Text("+ Custom Meal", color = AmberPrimary, style = MaterialTheme.typography.labelSmall)
                            }
                        }

                        val foodPresets = listOf(
                            Triple("3 Boiled Eggs", 210, 18f),
                            Triple("Chicken Breast & Rice", 650, 42f),
                            Triple("Paneer Bhurji Wrap", 480, 26f),
                            Triple("1 Scoop Whey Shake", 130, 25f),
                            Triple("Dal Tadka & 3 Rotis", 420, 18f),
                            Triple("Curd & Mixed Nuts", 220, 14f)
                        )

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            foodPresets.chunked(2).forEach { pair ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    pair.forEach { (name, kcal, protein) ->
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(DarkSurfaceVariant)
                                                .border(1.dp, DarkBorder, RoundedCornerShape(10.dp))
                                                .clickable {
                                                    viewModel.logMeal(name, "Meal", kcal, protein, 30f, 10f)
                                                }
                                                .padding(10.dp)
                                        ) {
                                            Column {
                                                Text(name, fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = TextPrimary)
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Text("${protein.toInt()}g P • $kcal kcal", fontSize = 11.sp, color = AmberPrimary)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Logged Meals for Today
                item {
                    Text(
                        text = "TODAY'S LOGGED MEALS (${nutritionLogs.size})",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                        color = TextPrimary,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
                    )
                }

                items(nutritionLogs, key = { it.id }) { meal ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 2.dp)
                    ) {
                        GlowCard(borderColor = DarkBorder) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(meal.mealName, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                                    Text(
                                        text = "${meal.proteinGrams.toInt()}g protein • ${meal.calories} kcal • ${meal.time}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = TextSecondary
                                    )
                                }
                                IconButton(onClick = { viewModel.deleteMeal(meal) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFFF5252), modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Log Workout Dialog
    if (showLogWorkoutDialog) {
        LogWorkoutDialog(
            onDismiss = { showLogWorkoutDialog = false },
            onConfirm = { name, duration, exercises, volume, notes, energy ->
                viewModel.logWorkout(name, duration, exercises, volume, notes, energy)
                showLogWorkoutDialog = false
            }
        )
    }

    // Custom Meal Dialog
    if (showCustomMealDialog) {
        CustomMealDialog(
            onDismiss = { showCustomMealDialog = false },
            onConfirm = { name, type, calories, protein, carbs, fats ->
                viewModel.logMeal(name, type, calories, protein, carbs, fats)
                showCustomMealDialog = false
            }
        )
    }
}

@Composable
fun WorkoutCard(workout: WorkoutLog) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        GlowCard(borderColor = DarkBorder) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = workout.routineName,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )
                    Text(
                        text = "${workout.durationMinutes} mins",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = AmberPrimary
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${workout.date} • Volume: ${workout.totalVolumeKg.toInt()} kg • Energy: ${workout.energyRating}/5",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )

                if (workout.notes.isNotBlank()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = workout.notes,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextPrimary
                    )
                }
            }
        }
    }
}

@Composable
fun LogWorkoutDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, Int, Int, Float, String, Int) -> Unit
) {
    var routineName by remember { mutableStateOf("Upper Body Push & Pull") }
    var durationMinutes by remember { mutableStateOf("60") }
    var exerciseCount by remember { mutableStateOf("5") }
    var totalVolumeKg by remember { mutableStateOf("4200") }
    var notes by remember { mutableStateOf("Bench 45kg 3x10, Lat Pulldown 50kg 3x10, DB Press 16kg 3x10") }
    var energyRating by remember { mutableIntStateOf(4) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DarkSurface,
        title = { Text("Log Workout Session", color = AmberPrimary, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = routineName,
                    onValueChange = { routineName = it },
                    label = { Text("Routine Name") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = AmberPrimary,
                        unfocusedBorderColor = DarkBorder
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("workout_routine_input")
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = durationMinutes,
                        onValueChange = { durationMinutes = it },
                        label = { Text("Duration (min)") },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = AmberPrimary,
                            unfocusedBorderColor = DarkBorder
                        )
                    )
                    OutlinedTextField(
                        value = totalVolumeKg,
                        onValueChange = { totalVolumeKg = it },
                        label = { Text("Volume (kg)") },
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
                    label = { Text("Sets, Reps & Weights Notes") },
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
                    onConfirm(
                        routineName,
                        durationMinutes.toIntOrNull() ?: 60,
                        exerciseCount.toIntOrNull() ?: 5,
                        totalVolumeKg.toFloatOrNull() ?: 4000f,
                        notes,
                        energyRating
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = AmberPrimary),
                modifier = Modifier.testTag("confirm_log_workout")
            ) {
                Text("Save Workout", color = Color(0xFF140C00))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel", color = TextSecondary) }
        }
    )
}

@Composable
fun CustomMealDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, Int, Float, Float, Float) -> Unit
) {
    var mealName by remember { mutableStateOf("") }
    var mealType by remember { mutableStateOf("Lunch") }
    var calories by remember { mutableStateOf("500") }
    var protein by remember { mutableStateOf("30") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DarkSurface,
        title = { Text("Log Custom Meal", color = AmberPrimary, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = mealName,
                    onValueChange = { mealName = it },
                    label = { Text("Food / Meal Name") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = AmberPrimary,
                        unfocusedBorderColor = DarkBorder
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("custom_meal_name_input")
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = calories,
                        onValueChange = { calories = it },
                        label = { Text("Calories (kcal)") },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = AmberPrimary,
                            unfocusedBorderColor = DarkBorder
                        )
                    )
                    OutlinedTextField(
                        value = protein,
                        onValueChange = { protein = it },
                        label = { Text("Protein (g)") },
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
                    if (mealName.isNotBlank()) {
                        onConfirm(
                            mealName,
                            mealType,
                            calories.toIntOrNull() ?: 400,
                            protein.toFloatOrNull() ?: 25f,
                            40f,
                            12f
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = AmberPrimary)
            ) {
                Text("Log Meal", color = Color(0xFF140C00))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel", color = TextSecondary) }
        }
    )
}
