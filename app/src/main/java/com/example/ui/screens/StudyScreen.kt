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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.School
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
import com.example.data.local.StudyTopic
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
import org.json.JSONArray

@Composable
fun StudyScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val topics by viewModel.studyTopics.collectAsState()
    val reviewQueue by viewModel.reviewQueue.collectAsState()

    var selectedTrack by remember { mutableStateOf("All") }
    var topicForQuiz by remember { mutableStateOf<StudyTopic?>(null) }
    var topicForTeachMe by remember { mutableStateOf<StudyTopic?>(null) }
    var showInterviewSimulator by remember { mutableStateOf(false) }
    var showEnglishPractice by remember { mutableStateOf(false) }

    val tracks = listOf(
        "All",
        "Python Programming",
        "NumPy & Pandas",
        "Mathematics & Statistics",
        "Supervised ML",
        "Ensemble Learning",
        "Deep Learning",
        "NLP & RAG",
        "Deployment & APIs",
        "GATE DA",
        "Interview Prep"
    )

    val filteredTopics = if (selectedTrack == "All") {
        topics
    } else {
        topics.filter { it.track.equals(selectedTrack, ignoreCase = true) }
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
                    title = "AI STUDY & CAREER LAB",
                    subtitle = "Mastery Engine & Interview Simulator"
                )
            }

            // Quick Tool Action Buttons: Interview Sim & English Practice
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { showInterviewSimulator = true },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("interview_simulator_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = AmberPrimary),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Psychology, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Mock Interview", style = MaterialTheme.typography.labelMedium)
                    }

                    OutlinedButton(
                        onClick = { showEnglishPractice = true },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("english_practice_button"),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
                    ) {
                        Icon(Icons.Default.RecordVoiceOver, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("English Drill", style = MaterialTheme.typography.labelMedium)
                    }
                }
            }

            // Spaced Repetition Queue Banner
            if (reviewQueue.isNotEmpty()) {
                item {
                    Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                        GlowCard(
                            borderColor = Color(0xFFFFB300).copy(alpha = 0.5f),
                            backgroundColor = Color(0xFF1E1708)
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "SPACED REPETITION QUEUE (${reviewQueue.size})",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                                        color = AmberGlow
                                    )
                                    Text(
                                        text = "Ready to Revise",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = TextSecondary
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = reviewQueue.firstOrNull()?.title ?: "",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = { topicForQuiz = reviewQueue.firstOrNull() },
                                    colors = ButtonDefaults.buttonColors(containerColor = AmberPrimary),
                                    modifier = Modifier.height(34.dp),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text("Test Knowledge Now", fontSize = 11.sp, color = Color(0xFF140C00))
                                }
                            }
                        }
                    }
                }
            }

            // Track Filter Chips
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 2.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    tracks.forEach { track ->
                        val isSelected = (selectedTrack == track)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) AmberPrimary else DarkSurfaceVariant)
                                .border(1.dp, if (isSelected) AmberGlow else DarkBorder, RoundedCornerShape(8.dp))
                                .clickable { selectedTrack = track }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = track,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = if (isSelected) Color(0xFF140C00) else TextPrimary
                            )
                        }
                    }
                }
            }

            // Topics List
            items(filteredTopics, key = { it.id }) { topic ->
                TopicCard(
                    topic = topic,
                    onQuizClick = { topicForQuiz = topic },
                    onTeachMeClick = { topicForTeachMe = topic }
                )
            }
        }
    }

    // Knowledge Check Quiz Dialog
    if (topicForQuiz != null) {
        QuizDialog(
            topic = topicForQuiz!!,
            onDismiss = { topicForQuiz = null },
            onSubmitAnswer = { selectedIdx ->
                viewModel.submitQuizAnswer(topicForQuiz!!, selectedIdx)
                topicForQuiz = null
            }
        )
    }

    // Interactive AI Tutor ("Teach Me") Dialog
    if (topicForTeachMe != null) {
        TeachMeDialog(
            topic = topicForTeachMe!!,
            onDismiss = { topicForTeachMe = null },
            onAskAi = { query ->
                viewModel.askAiCoach(query, "TEACH_ME")
            }
        )
    }

    // Mock Interview Simulator Dialog
    if (showInterviewSimulator) {
        InterviewSimulatorDialog(
            onDismiss = { showInterviewSimulator = false },
            onEvaluate = { userSpeech ->
                viewModel.askAiCoach("Evaluate my technical interview answer:\n$userSpeech", "INTERVIEW_SIMULATOR")
            }
        )
    }

    // Spoken English Practice Dialog
    if (showEnglishPractice) {
        EnglishPracticeDialog(
            onDismiss = { showEnglishPractice = false },
            onEvaluate = { spokenText ->
                viewModel.askAiCoach("Improve my English fluency and articulation:\n$spokenText", "ENGLISH_PRACTICE")
            }
        )
    }
}

@Composable
fun TopicCard(
    topic: StudyTopic,
    onQuizClick: () -> Unit,
    onTeachMeClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 2.dp)
    ) {
        GlowCard(
            borderColor = when (topic.status) {
                "MASTERED" -> EmeraldSuccess.copy(alpha = 0.4f)
                "IN_PROGRESS" -> AmberPrimary.copy(alpha = 0.4f)
                else -> DarkBorder
            },
            backgroundColor = DarkSurfaceCard
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { expanded = !expanded }
                    ) {
                        Text(
                            text = topic.track.uppercase(),
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, letterSpacing = 0.5.sp),
                            color = AmberGlow
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = topic.title,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(
                                when (topic.status) {
                                    "MASTERED" -> Color(0xFF0F2E1E)
                                    "IN_PROGRESS" -> Color(0xFF332008)
                                    else -> DarkSurfaceVariant
                                }
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${topic.masteryScore}%",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = when (topic.status) {
                                "MASTERED" -> EmeraldSuccess
                                "IN_PROGRESS" -> AmberPrimary
                                else -> TextSecondary
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = topic.explanation,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    maxLines = if (expanded) Int.MAX_VALUE else 2
                )

                AnimatedVisibility(visible = expanded) {
                    Column(modifier = Modifier.padding(top = 10.dp)) {
                        Text(
                            text = "Practical Exercise:",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = AmberPrimary
                        )
                        Text(
                            text = topic.practicalExercise,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextPrimary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Interview Questions:",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = AmberPrimary
                        )
                        Text(
                            text = topic.interviewQuestions,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = onQuizClick,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(36.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = AmberPrimary),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Icon(Icons.Default.Quiz, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Take Quiz", fontSize = 11.sp, color = Color(0xFF140C00))
                            }

                            OutlinedButton(
                                onClick = onTeachMeClick,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(36.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                                shape = RoundedCornerShape(6.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
                            ) {
                                Icon(Icons.Default.School, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Teach Me AI", fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuizDialog(
    topic: StudyTopic,
    onDismiss: () -> Unit,
    onSubmitAnswer: (Int) -> Unit
) {
    var selectedOption by remember { mutableStateOf<Int?>(null) }
    var hasSubmitted by remember { mutableStateOf(false) }

    val options = remember(topic.quizOptionsJson) {
        try {
            val jsonArray = JSONArray(topic.quizOptionsJson)
            val list = mutableListOf<String>()
            for (i in 0 until jsonArray.length()) {
                list.add(jsonArray.getString(i))
            }
            list
        } catch (e: Exception) {
            listOf("Option A", "Option B", "Option C", "Option D")
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DarkSurface,
        title = {
            Text("Knowledge Check: ${topic.title}", color = AmberPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(topic.quizQuestion, color = TextPrimary, fontWeight = FontWeight.Medium)

                Spacer(modifier = Modifier.height(4.dp))

                options.forEachIndexed { index, optionText ->
                    val isSelected = (selectedOption == index)
                    val isCorrect = (index == topic.correctOptionIndex)
                    val bgColor = when {
                        hasSubmitted && isCorrect -> Color(0xFF0D331E)
                        hasSubmitted && isSelected && !isCorrect -> Color(0xFF421019)
                        isSelected -> AmberPrimary.copy(alpha = 0.2f)
                        else -> DarkSurfaceVariant
                    }
                    val borderColor = when {
                        hasSubmitted && isCorrect -> EmeraldSuccess
                        hasSubmitted && isSelected && !isCorrect -> Color(0xFFFF5252)
                        isSelected -> AmberPrimary
                        else -> DarkBorder
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(bgColor)
                            .border(1.dp, borderColor, RoundedCornerShape(8.dp))
                            .clickable(enabled = !hasSubmitted) { selectedOption = index }
                            .padding(12.dp)
                    ) {
                        Text(
                            text = optionText,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextPrimary
                        )
                    }
                }

                if (hasSubmitted) {
                    val correct = selectedOption == topic.correctOptionIndex
                    Text(
                        text = if (correct) "✓ Correct! Mastery updated (+20%)." else "✗ Incorrect. Review explanation to reinforce concept.",
                        color = if (correct) EmeraldSuccess else Color(0xFFFF5252),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            if (!hasSubmitted) {
                Button(
                    onClick = {
                        if (selectedOption != null) {
                            hasSubmitted = true
                        }
                    },
                    enabled = selectedOption != null,
                    colors = ButtonDefaults.buttonColors(containerColor = AmberPrimary)
                ) {
                    Text("Check Answer", color = Color(0xFF140C00))
                }
            } else {
                Button(
                    onClick = {
                        selectedOption?.let { onSubmitAnswer(it) }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AmberPrimary)
                ) {
                    Text("Continue", color = Color(0xFF140C00))
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Close", color = TextSecondary) }
        }
    )
}

@Composable
fun TeachMeDialog(
    topic: StudyTopic,
    onDismiss: () -> Unit,
    onAskAi: (String) -> Unit
) {
    var userQuestion by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DarkSurface,
        title = {
            Text("AI Tutor: ${topic.title}", color = AmberPrimary, fontWeight = FontWeight.Bold)
        },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Core Concept Breakdown:",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = AmberGlow
                )
                Text(topic.explanation, color = TextPrimary, style = MaterialTheme.typography.bodySmall)

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Ask AI Tutor a Specific Doubt:",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = AmberGlow
                )
                OutlinedTextField(
                    value = userQuestion,
                    onValueChange = { userQuestion = it },
                    placeholder = { Text("e.g. Can you explain this with a real Python code example?") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = AmberPrimary,
                        unfocusedBorderColor = DarkBorder
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val prompt = "Explain ${topic.title} in simple language for an aspiring AI engineer. ${if (userQuestion.isNotBlank()) "Specific question: $userQuestion" else ""}"
                    onAskAi(prompt)
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = AmberPrimary)
            ) {
                Text("Ask AI Coach", color = Color(0xFF140C00))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Done", color = TextSecondary) }
        }
    )
}

@Composable
fun InterviewSimulatorDialog(
    onDismiss: () -> Unit,
    onEvaluate: (String) -> Unit
) {
    var answerText by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DarkSurface,
        title = { Text("🎙️ AI Engineering Mock Interview", color = AmberPrimary, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Interview Prompt:",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = AmberGlow
                )
                Text(
                    text = "\"How do you measure and mitigate data leakage in feature engineering for a high-frequency recommendation model?\"",
                    color = TextPrimary,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Type or transcribe your response:",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
                OutlinedTextField(
                    value = answerText,
                    onValueChange = { answerText = it },
                    minLines = 4,
                    placeholder = { Text("Structure with STAR: Situation, Task, Action, Result...") },
                    modifier = Modifier.fillMaxWidth().testTag("interview_answer_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = AmberPrimary,
                        unfocusedBorderColor = DarkBorder
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (answerText.isNotBlank()) {
                        onEvaluate(answerText)
                        onDismiss()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = AmberPrimary),
                modifier = Modifier.testTag("submit_interview_answer")
            ) {
                Text("Evaluate with AI", color = Color(0xFF140C00))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel", color = TextSecondary) }
        }
    )
}

@Composable
fun EnglishPracticeDialog(
    onDismiss: () -> Unit,
    onEvaluate: (String) -> Unit
) {
    var spokenDraft by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DarkSurface,
        title = { Text("🗣️ Spoken English Fluency Lab", color = AmberPrimary, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Vocal Drill Prompt:",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = AmberGlow
                )
                Text(
                    text = "\"Describe the architecture of your hybrid movie recommendation engine in 60 seconds without filler words like 'um', 'actually', or 'basically'.\"",
                    color = TextPrimary,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(4.dp))

                OutlinedTextField(
                    value = spokenDraft,
                    onValueChange = { spokenDraft = it },
                    minLines = 3,
                    placeholder = { Text("Enter your spoken answer transcript here...") },
                    modifier = Modifier.fillMaxWidth().testTag("english_spoken_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = AmberPrimary,
                        unfocusedBorderColor = DarkBorder
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (spokenDraft.isNotBlank()) {
                        onEvaluate(spokenDraft)
                        onDismiss()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = AmberPrimary),
                modifier = Modifier.testTag("submit_english_practice")
            ) {
                Text("Get Articulation Feedback", color = Color(0xFF140C00))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel", color = TextSecondary) }
        }
    )
}
