package com.example.ui.screens

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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.data.local.ChatMessageEntity
import com.example.ui.components.MohoragaTopBar
import com.example.ui.theme.AmberGlow
import com.example.ui.theme.AmberPrimary
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.MainViewModel

@Composable
fun AiCoachScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val messages by viewModel.chatMessages.collectAsState()
    val isAiLoading by viewModel.isAiLoading.collectAsState()
    val profile by viewModel.userProfile.collectAsState()
    val selectedRole by viewModel.selectedRole.collectAsState()

    var userPromptText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    val roles = listOf(
        Triple("LIFE_COACH", "Life Coach (3.5-flash)", Icons.Default.AutoAwesome),
        Triple("SCIENCE_SPECIALIST", "Science & STEM (3.1-pro)", Icons.Default.Science),
        Triple("RAPID_ASSISTANT", "Rapid Tactical (3.1-lite)", Icons.Default.Speed)
    )

    val quickActionChips = listOf(
        "Plan My Day" to "PLAN_DAY",
        "Rebuild My Schedule" to "REBUILD_SCHEDULE",
        "What Should I Do Next?" to "WHAT_NEXT",
        "Find My Weaknesses" to "FIND_WEAKNESSES",
        "Prepare Me for Interviews" to "INTERVIEW_SIMULATOR",
        "Explain My Progress" to "GENERAL",
        "Help Me Refocus" to "GENERAL"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 74.dp)
    ) {
        MohoragaTopBar(
            title = "GEMINI AI ARCHITECT",
            subtitle = "Multi-Turn Science & Life OS Engine",
            actionIcon = Icons.Default.DeleteSweep,
            onActionClick = { viewModel.clearChatHistory() }
        )

        // Model & Role Selector Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            roles.forEach { (roleKey, roleLabel, roleIcon) ->
                val isSelected = (selectedRole == roleKey)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSelected) AmberPrimary else DarkSurfaceVariant)
                        .border(1.dp, if (isSelected) AmberGlow else DarkBorder, RoundedCornerShape(10.dp))
                        .clickable { viewModel.setCoachRole(roleKey) }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .testTag("ai_role_selector_$roleKey")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = roleIcon,
                            contentDescription = null,
                            tint = if (isSelected) Color(0xFF140C00) else AmberPrimary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = roleLabel,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = if (isSelected) Color(0xFF140C00) else TextPrimary
                        )
                    }
                }
            }
        }

        // Action Chips Carousel
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            quickActionChips.forEach { (label, actionType) ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(DarkSurfaceVariant)
                        .border(1.dp, DarkBorder, RoundedCornerShape(8.dp))
                        .clickable {
                            viewModel.askAiCoach(label, actionType)
                        }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                        .testTag("ai_action_${actionType}")
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                }
            }
        }

        // Messages Thread (Persistent in Room)
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(messages, key = { it.id }) { msg ->
                ChatBubble(msg = msg, userName = profile?.name ?: "Mohoraga")
            }

            if (isAiLoading) {
                item {
                    Row(
                        modifier = Modifier.padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            color = AmberPrimary,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Gemini is reasoning and synthesizing response...",
                            style = MaterialTheme.typography.labelSmall,
                            color = AmberGlow
                        )
                    }
                }
            }
        }

        // Input Field Bar
        Surface(
            color = DarkSurfaceCard,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, DarkBorder, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = userPromptText,
                    onValueChange = { userPromptText = it },
                    placeholder = { Text("Ask Science doubts, ML algorithms, or day plan...", fontSize = 12.sp) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("ai_coach_input"),
                    shape = RoundedCornerShape(12.dp),
                    maxLines = 4,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = AmberPrimary,
                        unfocusedBorderColor = DarkBorder,
                        focusedContainerColor = DarkSurface,
                        unfocusedContainerColor = DarkSurface
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        if (userPromptText.isNotBlank()) {
                            val text = userPromptText
                            userPromptText = ""
                            viewModel.askAiCoach(text, "GENERAL")
                        }
                    },
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(AmberPrimary)
                        .testTag("ai_coach_send_button")
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        tint = Color(0xFF140C00),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ChatBubble(
    msg: ChatMessageEntity,
    userName: String
) {
    val isUser = msg.sender == "USER"

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = if (isUser) userName else "Mohoraga Architect",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = if (isUser) AmberGlow else CyberCyan
            )
            if (!isUser) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(DarkSurfaceVariant)
                        .padding(horizontal = 6.dp, vertical = 1.dp)
                ) {
                    Text(
                        text = msg.modelUsed,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                        color = TextMuted
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Box(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = 14.dp,
                        topEnd = 14.dp,
                        bottomStart = if (isUser) 14.dp else 4.dp,
                        bottomEnd = if (isUser) 4.dp else 14.dp
                    )
                )
                .background(if (isUser) AmberPrimary.copy(alpha = 0.2f) else DarkSurfaceVariant)
                .border(
                    1.dp,
                    if (isUser) AmberPrimary.copy(alpha = 0.4f) else DarkBorder,
                    RoundedCornerShape(
                        topStart = 14.dp,
                        topEnd = 14.dp,
                        bottomStart = if (isUser) 14.dp else 4.dp,
                        bottomEnd = if (isUser) 4.dp else 14.dp
                    )
                )
                .padding(14.dp)
        ) {
            Text(
                text = msg.message,
                style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                color = TextPrimary
            )
        }
    }
}
