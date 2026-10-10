package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.DailyTask
import com.example.data.local.FocusSession
import com.example.data.local.Goal
import com.example.data.local.GoalMilestone
import com.example.data.local.Habit
import com.example.data.local.HabitLog
import com.example.data.local.JournalEntry
import com.example.data.local.NutritionLog
import com.example.data.local.SeedData
import com.example.data.local.StudyTopic
import com.example.data.local.UserProfile
import com.example.data.local.WaterLog
import com.example.data.local.WorkoutLog
import com.example.data.repository.AppRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppScreen(val label: String) {
    TODAY("Today"),
    GOALS("Goals"),
    STUDY("Study & Career"),
    FITNESS("Fitness"),
    HABITS("Habits"),
    AI_COACH("AI Coach"),
    ANALYTICS("Analytics"),
    JOURNAL("Journal"),
    SETTINGS("Settings")
}

data class ChatMessage(
    val sender: String, // "USER" or "AI"
    val message: String,
    val timestamp: Long = System.currentTimeMillis()
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val repository = AppRepository(db)

    val todayDate: String = SeedData.getTodayString()

    // Navigation
    private val _currentScreen = MutableStateFlow(AppScreen.TODAY)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    // User Profile
    val userProfile: StateFlow<UserProfile?> = repository.userProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SeedData.defaultProfile)

    // Tasks
    val tasks: StateFlow<List<DailyTask>> = repository.getTasksForDate(todayDate)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Goals & Milestones
    val goals: StateFlow<List<Goal>> = repository.allGoals
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val milestones: StateFlow<List<GoalMilestone>> = repository.allMilestones
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Habits
    val habits: StateFlow<List<Habit>> = repository.allHabits
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val habitLogs: StateFlow<List<HabitLog>> = repository.getHabitLogsForDate(todayDate)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Study
    val studyTopics: StateFlow<List<StudyTopic>> = repository.allTopics
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val reviewQueue: StateFlow<List<StudyTopic>> = repository.getReviewQueue(todayDate)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Fitness
    val workouts: StateFlow<List<WorkoutLog>> = repository.allWorkouts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val nutritionLogs: StateFlow<List<NutritionLog>> = repository.getNutritionForDate(todayDate)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val waterLog: StateFlow<WaterLog?> = repository.getWaterLogForDate(todayDate)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Journal
    val journalEntries: StateFlow<List<JournalEntry>> = repository.allJournalEntries
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val lessonsLearned: StateFlow<List<JournalEntry>> = repository.lessonsLearned
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // AI Coach Chat
    val chatMessages: StateFlow<List<com.example.data.local.ChatMessageEntity>> = repository.allChatMessages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedRole = MutableStateFlow("LIFE_COACH")
    val selectedRole: StateFlow<String> = _selectedRole.asStateFlow()

    fun setCoachRole(role: String) {
        _selectedRole.value = role
    }

    private val _isAiLoading = MutableStateFlow(false)
    val isAiLoading: StateFlow<Boolean> = _isAiLoading.asStateFlow()

    // Focus Timer State
    private val _isTimerRunning = MutableStateFlow(false)
    val isTimerRunning: StateFlow<Boolean> = _isTimerRunning.asStateFlow()

    private val _timerSecondsLeft = MutableStateFlow(25 * 60)
    val timerSecondsLeft: StateFlow<Int> = _timerSecondsLeft.asStateFlow()

    private val _activeTimerTask = MutableStateFlow("Deep Work Sprint")
    val activeTimerTask: StateFlow<String> = _activeTimerTask.asStateFlow()

    private var timerJob: Job? = null

    // Energy & Mood Check-In for Today
    private val _todayMood = MutableStateFlow("Focused")
    val todayMood: StateFlow<String> = _todayMood.asStateFlow()

    private val _todayEnergy = MutableStateFlow(4) // 1-5
    val todayEnergy: StateFlow<Int> = _todayEnergy.asStateFlow()

    init {
        // Ensure seeding on start
        viewModelScope.launch {
            AppDatabase.seedDatabase(db)
        }
    }

    // --- Task Actions ---
    fun toggleTask(task: DailyTask) {
        viewModelScope.launch {
            repository.toggleTaskCompletion(task)
        }
    }

    fun addTask(
        title: String,
        category: String,
        startTime: String,
        endTime: String,
        durationMinutes: Int,
        priority: String,
        notes: String
    ) {
        viewModelScope.launch {
            val currentList = tasks.value
            val newTask = DailyTask(
                date = todayDate,
                title = title,
                category = category,
                startTime = startTime,
                endTime = endTime,
                durationMinutes = durationMinutes,
                priority = priority,
                notes = notes,
                orderIndex = currentList.size + 1
            )
            repository.addTask(newTask)
        }
    }

    fun rescheduleTask(task: DailyTask, newStartTime: String, newEndTime: String) {
        viewModelScope.launch {
            repository.rescheduleTask(task, newStartTime, newEndTime)
        }
    }

    fun deleteTask(task: DailyTask) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    // --- Goal & Milestone Actions ---
    fun toggleMilestone(milestone: GoalMilestone) {
        viewModelScope.launch {
            repository.toggleMilestoneCompletion(milestone)
        }
    }

    fun addGoal(title: String, description: String, reason: String, category: String, targetDate: String, priority: String, successCriteria: String) {
        viewModelScope.launch {
            val goal = Goal(
                title = title,
                description = description,
                reason = reason,
                category = category,
                targetDate = targetDate,
                priority = priority,
                progressPercentage = 0,
                successCriteria = successCriteria
            )
            repository.addGoal(goal)
        }
    }

    fun addMilestone(goalId: Long, title: String, targetDate: String) {
        viewModelScope.launch {
            repository.addMilestone(GoalMilestone(goalId = goalId, title = title, targetDate = targetDate))
        }
    }

    // --- Habit Actions ---
    fun toggleHabitToday(habit: Habit) {
        viewModelScope.launch {
            val isDone = habitLogs.value.any { it.habitId == habit.id }
            repository.toggleHabitForDate(habit, todayDate, isDone)
        }
    }

    fun addHabit(title: String, category: String, frequency: String, targetPerWeek: Int, reminderTime: String) {
        viewModelScope.launch {
            val habit = Habit(
                title = title,
                category = category,
                frequency = frequency,
                targetPerWeek = targetPerWeek,
                reminderTime = reminderTime
            )
            repository.addHabit(habit)
        }
    }

    // --- Study Actions ---
    fun submitQuizAnswer(topic: StudyTopic, selectedIndex: Int) {
        viewModelScope.launch {
            val isCorrect = (selectedIndex == topic.correctOptionIndex)
            val newMastery = if (isCorrect) (topic.masteryScore + 20).coerceAtMost(100) else (topic.masteryScore - 10).coerceAtLeast(0)
            val newStatus = if (newMastery >= 80) "MASTERED" else "IN_PROGRESS"
            repository.updateTopic(topic.copy(
                masteryScore = newMastery,
                status = newStatus,
                lastReviewedDate = todayDate
            ))
        }
    }

    // --- Fitness Actions ---
    fun logWorkout(routineName: String, durationMinutes: Int, exerciseCount: Int, totalVolumeKg: Float, notes: String, energyRating: Int) {
        viewModelScope.launch {
            val workout = WorkoutLog(
                date = todayDate,
                routineName = routineName,
                durationMinutes = durationMinutes,
                exerciseCount = exerciseCount,
                totalVolumeKg = totalVolumeKg,
                notes = notes,
                energyRating = energyRating
            )
            repository.addWorkout(workout)
        }
    }

    fun logMeal(mealName: String, mealType: String, calories: Int, protein: Float, carbs: Float, fats: Float) {
        viewModelScope.launch {
            val meal = NutritionLog(
                date = todayDate,
                mealName = mealName,
                mealType = mealType,
                calories = calories,
                proteinGrams = protein,
                carbsGrams = carbs,
                fatGrams = fats,
                time = SimpleTimeFormatter.getCurrentTimeFormatted()
            )
            repository.addNutrition(meal)
        }
    }

    fun deleteMeal(meal: NutritionLog) {
        viewModelScope.launch {
            repository.deleteNutrition(meal)
        }
    }

    fun incrementWater() {
        viewModelScope.launch {
            val current = waterLog.value?.glassesCount ?: 0
            repository.updateWaterGlasses(todayDate, current + 1)
        }
    }

    // --- Journal Actions ---
    fun deleteJournalEntry(entry: JournalEntry) {
        viewModelScope.launch {
            repository.deleteJournalEntry(entry)
        }
    }

    fun saveJournal(
        feeling: String,
        priority: String,
        distraction: String,
        win: String,
        improvement: String,
        mood: String,
        energy: Int,
        isLesson: Boolean,
        tags: String
    ) {
        viewModelScope.launch {
            val entry = JournalEntry(
                date = todayDate,
                mood = mood,
                energyLevel = energy,
                feelingAnswer = feeling,
                priorityAnswer = priority,
                distractionAnswer = distraction,
                winAnswer = win,
                improvementAnswer = improvement,
                isLessonLearned = isLesson,
                tags = tags
            )
            repository.addJournalEntry(entry)
        }
    }

    // --- Profile & Settings Actions ---
    fun updateProfile(profile: UserProfile) {
        viewModelScope.launch {
            repository.updateUserProfile(profile)
        }
    }

    fun resetData() {
        viewModelScope.launch {
            repository.resetAllDataToDefaults()
        }
    }

    fun exportDataJson(onExport: (String) -> Unit) {
        viewModelScope.launch {
            val json = repository.exportDataAsJson()
            onExport(json)
        }
    }

    // --- Focus Timer Actions ---
    fun startTimer(taskName: String = "Deep Work Focus", durationMinutes: Int = 25) {
        _activeTimerTask.value = taskName
        _timerSecondsLeft.value = durationMinutes * 60
        _isTimerRunning.value = true
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_timerSecondsLeft.value > 0 && _isTimerRunning.value) {
                delay(1000)
                _timerSecondsLeft.value -= 1
            }
            if (_timerSecondsLeft.value == 0) {
                _isTimerRunning.value = false
                repository.addFocusSession(FocusSession(
                    date = todayDate,
                    durationMinutes = durationMinutes,
                    taskTitle = taskName,
                    category = "Deep Work"
                ))
            }
        }
    }

    fun pauseTimer() {
        _isTimerRunning.value = false
        timerJob?.cancel()
    }

    fun resumeTimer() {
        if (_timerSecondsLeft.value > 0) {
            _isTimerRunning.value = true
            timerJob?.cancel()
            timerJob = viewModelScope.launch {
                while (_timerSecondsLeft.value > 0 && _isTimerRunning.value) {
                    delay(1000)
                    _timerSecondsLeft.value -= 1
                }
                if (_timerSecondsLeft.value == 0) {
                    _isTimerRunning.value = false
                }
            }
        }
    }

    fun resetTimer() {
        _isTimerRunning.value = false
        timerJob?.cancel()
        _timerSecondsLeft.value = 25 * 60
    }

    // --- AI Coach Prompting ---
    fun askAiCoach(prompt: String, actionType: String = "GENERAL") {
        _isAiLoading.value = true

        viewModelScope.launch {
            try {
                repository.getAiCoachAdvice(prompt, _selectedRole.value, actionType)
            } catch (e: Exception) {
                // Error handled gracefully
            } finally {
                _isAiLoading.value = false
            }
        }
    }

    fun clearChatHistory() {
        viewModelScope.launch {
            repository.clearChatHistory()
        }
    }

    fun updateCheckIn(mood: String, energy: Int) {
        _todayMood.value = mood
        _todayEnergy.value = energy
    }
}

object SimpleTimeFormatter {
    fun getCurrentTimeFormatted(): String {
        val sdf = java.text.SimpleDateFormat("hh:mm a", java.util.Locale.getDefault())
        return sdf.format(java.util.Date())
    }
}
