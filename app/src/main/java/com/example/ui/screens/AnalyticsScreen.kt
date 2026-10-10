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
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.example.ui.components.GlowCard
import com.example.ui.components.MetricBadge
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
fun AnalyticsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val tasks by viewModel.tasks.collectAsState()
    val habits by viewModel.habits.collectAsState()
    val habitLogs by viewModel.habitLogs.collectAsState()
    val studyTopics by viewModel.studyTopics.collectAsState()
    val workouts by viewModel.workouts.collectAsState()
    val profile by viewModel.userProfile.collectAsState()

    var showWeeklyReport by remember { mutableStateOf(false) }

    val completedTasks = tasks.count { it.isCompleted }
    val taskCompletionRate = if (tasks.isNotEmpty()) (completedTasks * 100) / tasks.size else 0

    val completedHabits = habits.count { habit -> habitLogs.any { it.habitId == habit.id } }
    val habitCompletionRate = if (habits.isNotEmpty()) (completedHabits * 100) / habits.size else 0

    val averageStudyMastery = if (studyTopics.isNotEmpty()) studyTopics.map { it.masteryScore }.average().toInt() else 0

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            MohoragaTopBar(
                title = "PERFORMANCE ANALYTICS",
                subtitle = "Weekly Trajectory & Compound Growth"
            )
        }

        // Top Metric Grid
        item {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricBadge(
                        label = "Task Completion",
                        value = "$taskCompletionRate%",
                        accentColor = AmberPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    MetricBadge(
                        label = "Habit Integrity",
                        value = "$habitCompletionRate%",
                        accentColor = EmeraldSuccess,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricBadge(
                        label = "Avg ML Mastery",
                        value = "$averageStudyMastery%",
                        accentColor = CyberCyan,
                        modifier = Modifier.weight(1f)
                    )
                    MetricBadge(
                        label = "Bodyweight Goal",
                        value = "${profile?.currentWeightKg?.toInt() ?: 65} / ${profile?.targetWeightKg?.toInt() ?: 72} kg",
                        accentColor = AmberGlow,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Deep Work & Study Distribution
        item {
            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                GlowCard(borderColor = DarkBorder) {
                    Column {
                        Text(
                            text = "DOMAIN FOCUS DISTRIBUTION",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                            color = AmberGlow
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        val domains = listOf(
                            Triple("AI/ML Engineering & Recommender", 40, AmberPrimary),
                            Triple("GATE DA Math & Probability", 25, CyberCyan),
                            Triple("College Coursework & Labs", 20, Color(0xFFB388FF)),
                            Triple("Hypertrophy Fitness & Recovery", 15, EmeraldSuccess)
                        )

                        domains.forEach { (name, percent, color) ->
                            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(name, style = MaterialTheme.typography.bodySmall, color = TextPrimary)
                                    Text("$percent%", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = color)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                LinearProgressIndicator(
                                    progress = { percent / 100f },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp)
                                        .clip(RoundedCornerShape(3.dp)),
                                    color = color,
                                    trackColor = DarkBorder
                                )
                            }
                        }
                    }
                }
            }
        }

        // Weekly Report Section
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
                            Text(
                                text = "WEEKLY EXECUTIVE REPORT",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                                color = AmberPrimary
                            )
                            Icon(Icons.Default.Assessment, contentDescription = null, tint = AmberPrimary, modifier = Modifier.size(20.dp))
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "🏆 Major Achievements: Deployed TF-IDF matrix factorization foundation for recommendation engine; maintained 4-day habit streak on morning protein.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextPrimary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "⚠️ Friction Area: Mid-day post-college transition has occasional 30m idle lag before afternoon deep work session begins.",
                            style = MaterialTheme.typography.bodySmall,
                            color = AmberGlow
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "🎯 Next Week Priorities:\n1. Finish hybrid recommender latency benchmark under 50ms.\n2. Complete 50 GATE Bayes theorem & random variable practice problems.\n3. Hit 135g protein target every single day.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                viewModel.askAiCoach("Generate a comprehensive weekly synthesis of my progress and 3 strategic priorities for next week.", "GENERAL")
                                viewModel.navigateTo(com.example.ui.viewmodel.AppScreen.AI_COACH)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = AmberPrimary),
                            modifier = Modifier.fillMaxWidth().testTag("ai_generate_weekly_report"),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color(0xFF140C00))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Deep AI Report Analysis", color = Color(0xFF140C00))
                        }
                    }
                }
            }
        }
    }
}
