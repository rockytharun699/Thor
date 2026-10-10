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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.example.data.local.JournalEntry
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
fun JournalScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val entries by viewModel.journalEntries.collectAsState()
    val lessonsLearned by viewModel.lessonsLearned.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedTab by remember { mutableIntStateOf(0) } // 0: All Entries, 1: Lessons Learned
    var showNewEntryDialog by remember { mutableStateOf(false) }

    val activeList = if (selectedTab == 0) entries else lessonsLearned
    val filteredEntries = if (searchQuery.isBlank()) {
        activeList
    } else {
        activeList.filter {
            it.feelingAnswer.contains(searchQuery, ignoreCase = true) ||
            it.priorityAnswer.contains(searchQuery, ignoreCase = true) ||
            it.winAnswer.contains(searchQuery, ignoreCase = true) ||
            it.tags.contains(searchQuery, ignoreCase = true)
        }
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
                    title = "JOURNAL & KNOWLEDGE",
                    subtitle = "Structured Reflection & Lessons Learned"
                )
            }

            // Search Bar
            item {
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search reflections, insights, tags...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextSecondary) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("journal_search_input"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = AmberPrimary,
                            unfocusedBorderColor = DarkBorder,
                            focusedContainerColor = DarkSurfaceCard,
                            unfocusedContainerColor = DarkSurfaceCard
                        )
                    )
                }
            }

            // Tab Filter
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
                            text = { Text("All Reflections (${entries.size})", fontWeight = FontWeight.SemiBold, fontSize = 12.sp) }
                        )
                        Tab(
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            text = { Text("Lessons Learned (${lessonsLearned.size})", fontWeight = FontWeight.SemiBold, fontSize = 12.sp) }
                        )
                    }
                }
            }

            if (filteredEntries.isEmpty()) {
                item {
                    Box(modifier = Modifier.padding(16.dp)) {
                        GlowCard(borderColor = DarkBorder) {
                            Text(
                                text = "No reflection entries found. Tap '+' to write today's 5-question review.",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                        }
                    }
                }
            } else {
                items(filteredEntries, key = { it.id }) { entry ->
                    JournalEntryCard(
                        entry = entry,
                        onDelete = { viewModel.deleteJournalEntry(entry) }
                    )
                }
            }
        }

        FloatingActionButton(
            onClick = { showNewEntryDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 90.dp, end = 20.dp)
                .testTag("floating_add_journal"),
            containerColor = AmberPrimary,
            contentColor = Color(0xFF140C00)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Journal")
        }
    }

    if (showNewEntryDialog) {
        NewJournalDialog(
            onDismiss = { showNewEntryDialog = false },
            onSave = { feeling, priority, distraction, win, improvement, mood, energy, isLesson, tags ->
                viewModel.saveJournal(feeling, priority, distraction, win, improvement, mood, energy, isLesson, tags)
                showNewEntryDialog = false
            }
        )
    }
}

@Composable
fun JournalEntryCard(
    entry: JournalEntry,
    onDelete: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        GlowCard(
            borderColor = if (entry.isLessonLearned) AmberPrimary.copy(alpha = 0.5f) else DarkBorder
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (entry.isLessonLearned) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = AmberPrimary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                        }
                        Text(
                            text = "${entry.date} • ${entry.mood} (${entry.energyLevel}⚡)",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )
                    }

                    Text(
                        text = if (expanded) "Collapse" else "Expand",
                        style = MaterialTheme.typography.labelSmall,
                        color = AmberPrimary,
                        modifier = Modifier.clickable { expanded = !expanded }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "🏆 Win: ${entry.winAnswer}",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                    color = TextPrimary
                )

                AnimatedVisibility(visible = expanded) {
                    Column(
                        modifier = Modifier.padding(top = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("1. Feeling: ${entry.feelingAnswer}", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                        Text("2. What Matters: ${entry.priorityAnswer}", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                        Text("3. Distractions: ${entry.distractionAnswer}", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                        Text("4. Improvement: ${entry.improvementAnswer}", style = MaterialTheme.typography.bodySmall, color = AmberGlow)

                        if (entry.tags.isNotBlank()) {
                            Text("Tags: ${entry.tags}", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NewJournalDialog(
    onDismiss: () -> Unit,
    onSave: (String, String, String, String, String, String, Int, Boolean, String) -> Unit
) {
    var feeling by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf("") }
    var distraction by remember { mutableStateOf("") }
    var win by remember { mutableStateOf("") }
    var improvement by remember { mutableStateOf("") }
    var mood by remember { mutableStateOf("Focused") }
    var energy by remember { mutableIntStateOf(4) }
    var isLesson by remember { mutableStateOf(false) }
    var tags by remember { mutableStateOf("#ai #consistency") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DarkSurface,
        title = { Text("5-Question Daily Reflection", color = AmberPrimary, fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = feeling,
                    onValueChange = { feeling = it },
                    label = { Text("1. How am I feeling today?") },
                    modifier = Modifier.fillMaxWidth().testTag("journal_q1_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = AmberPrimary,
                        unfocusedBorderColor = DarkBorder
                    )
                )

                OutlinedTextField(
                    value = priority,
                    onValueChange = { priority = it },
                    label = { Text("2. What matters most today?") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = AmberPrimary,
                        unfocusedBorderColor = DarkBorder
                    )
                )

                OutlinedTextField(
                    value = distraction,
                    onValueChange = { distraction = it },
                    label = { Text("3. What is distracting me?") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = AmberPrimary,
                        unfocusedBorderColor = DarkBorder
                    )
                )

                OutlinedTextField(
                    value = win,
                    onValueChange = { win = it },
                    label = { Text("4. What did I do well?") },
                    modifier = Modifier.fillMaxWidth().testTag("journal_q4_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = AmberPrimary,
                        unfocusedBorderColor = DarkBorder
                    )
                )

                OutlinedTextField(
                    value = improvement,
                    onValueChange = { improvement = it },
                    label = { Text("5. What can I improve tomorrow?") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = AmberPrimary,
                        unfocusedBorderColor = DarkBorder
                    )
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    androidx.compose.material3.Checkbox(
                        checked = isLesson,
                        onCheckedChange = { isLesson = it }
                    )
                    Text("Pin as a key 'Lesson Learned'", style = MaterialTheme.typography.bodySmall, color = TextPrimary)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (win.isNotBlank() || feeling.isNotBlank()) {
                        onSave(feeling, priority, distraction, win, improvement, mood, energy, isLesson, tags)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = AmberPrimary),
                modifier = Modifier.testTag("save_journal_button")
            ) {
                Text("Save Reflection", color = Color(0xFF140C00))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel", color = TextSecondary) }
        }
    )
}
