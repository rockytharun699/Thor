package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.screens.AiCoachScreen
import com.example.ui.screens.AnalyticsScreen
import com.example.ui.screens.FitnessScreen
import com.example.ui.screens.GoalsScreen
import com.example.ui.screens.HabitsScreen
import com.example.ui.screens.JournalScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.StudyScreen
import com.example.ui.screens.TodayScreen
import com.example.ui.theme.AmberGlow
import com.example.ui.theme.AmberPrimary
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.MohoragaTheme
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MohoragaTheme {
                MohoragaApp(viewModel = viewModel)
            }
        }
    }
}

data class NavItem(
    val screen: AppScreen,
    val icon: ImageVector,
    val label: String
)

val navItems = listOf(
    NavItem(AppScreen.TODAY, Icons.Default.WbSunny, "Today"),
    NavItem(AppScreen.GOALS, Icons.Default.Flag, "Goals"),
    NavItem(AppScreen.STUDY, Icons.Default.School, "Study"),
    NavItem(AppScreen.FITNESS, Icons.Default.FitnessCenter, "Fitness"),
    NavItem(AppScreen.HABITS, Icons.Default.LocalFireDepartment, "Habits"),
    NavItem(AppScreen.AI_COACH, Icons.Default.AutoAwesome, "Coach"),
    NavItem(AppScreen.ANALYTICS, Icons.Default.Assessment, "Analytics"),
    NavItem(AppScreen.JOURNAL, Icons.Default.Book, "Journal"),
    NavItem(AppScreen.SETTINGS, Icons.Default.Settings, "Settings")
)

@Composable
fun MohoragaApp(viewModel: MainViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()

    // Back handling: If on secondary tab, back navigates to TODAY
    if (currentScreen != AppScreen.TODAY) {
        BackHandler {
            viewModel.navigateTo(AppScreen.TODAY)
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize().background(DarkBackground)) {
        val isWideScreen = maxWidth > 600.dp

        if (isWideScreen) {
            // Adaptive Tablet / Desktop Layout: Navigation Rail on Left
            Row(modifier = Modifier.fillMaxSize()) {
                NavigationRail(
                    containerColor = DarkSurface,
                    contentColor = TextPrimary,
                    header = {
                        Box(
                            modifier = Modifier
                                .padding(vertical = 16.dp)
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(AmberPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("M", fontWeight = FontWeight.Bold, color = Color(0xFF140C00))
                        }
                    },
                    modifier = Modifier
                        .fillMaxHeight()
                        .border(1.dp, DarkBorder, RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp))
                ) {
                    navItems.forEach { item ->
                        val isSelected = (currentScreen == item.screen)
                        NavigationRailItem(
                            selected = isSelected,
                            onClick = { viewModel.navigateTo(item.screen) },
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label, fontSize = 10.sp) },
                            colors = NavigationRailItemDefaults.colors(
                                selectedIconColor = Color(0xFF140C00),
                                selectedTextColor = AmberPrimary,
                                indicatorColor = AmberPrimary,
                                unselectedIconColor = TextSecondary,
                                unselectedTextColor = TextSecondary
                            )
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .windowInsetsPadding(WindowInsets.safeDrawing)
                ) {
                    ScreenContent(currentScreen = currentScreen, viewModel = viewModel)
                }
            }
        } else {
            // Mobile Layout: Scaffold with scrollable bottom nav
            Scaffold(
                containerColor = DarkBackground,
                contentWindowInsets = WindowInsets.safeDrawing,
                bottomBar = {
                    Surface(
                        color = DarkSurface,
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, DarkBorder, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState())
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            navItems.forEach { item ->
                                val isSelected = (currentScreen == item.screen)
                                val itemTag = "nav_item_${item.label.lowercase()}"

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (isSelected) AmberPrimary else Color.Transparent)
                                        .clickable { viewModel.navigateTo(item.screen) }
                                        .padding(horizontal = 12.dp, vertical = 8.dp)
                                        .testTag(itemTag)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            item.icon,
                                            contentDescription = item.label,
                                            tint = if (isSelected) Color(0xFF140C00) else TextSecondary,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        if (isSelected) {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = item.label,
                                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                color = Color(0xFF140C00)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    ScreenContent(currentScreen = currentScreen, viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun ScreenContent(
    currentScreen: AppScreen,
    viewModel: MainViewModel
) {
    AnimatedContent(
        targetState = currentScreen,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "ScreenTransition"
    ) { screen ->
        when (screen) {
            AppScreen.TODAY -> TodayScreen(viewModel = viewModel)
            AppScreen.GOALS -> GoalsScreen(viewModel = viewModel)
            AppScreen.STUDY -> StudyScreen(viewModel = viewModel)
            AppScreen.FITNESS -> FitnessScreen(viewModel = viewModel)
            AppScreen.HABITS -> HabitsScreen(viewModel = viewModel)
            AppScreen.AI_COACH -> AiCoachScreen(viewModel = viewModel)
            AppScreen.ANALYTICS -> AnalyticsScreen(viewModel = viewModel)
            AppScreen.JOURNAL -> JournalScreen(viewModel = viewModel)
            AppScreen.SETTINGS -> SettingsScreen(viewModel = viewModel)
        }
    }
}
