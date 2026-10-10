package com.example.data.repository

import com.example.data.ai.GeminiCoachService
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
import kotlinx.coroutines.flow.Flow
import org.json.JSONArray
import org.json.JSONObject

class AppRepository(
    private val db: AppDatabase,
    private val aiService: GeminiCoachService = GeminiCoachService()
) {
    // User Profile
    val userProfile: Flow<UserProfile?> = db.userDao().getUserProfile()

    suspend fun getUserProfileOnce(): UserProfile? = db.userDao().getUserProfileOnce()

    suspend fun updateUserProfile(profile: UserProfile) {
        db.userDao().insertOrUpdateProfile(profile)
    }

    // Daily Tasks
    fun getTasksForDate(date: String): Flow<List<DailyTask>> = db.taskDao().getTasksForDate(date)

    suspend fun getTasksForDateOnce(date: String): List<DailyTask> = db.taskDao().getTasksForDateOnce(date)

    suspend fun addTask(task: DailyTask): Long = db.taskDao().insertTask(task)

    suspend fun updateTask(task: DailyTask) = db.taskDao().updateTask(task)

    suspend fun deleteTask(task: DailyTask) = db.taskDao().deleteTask(task)

    suspend fun toggleTaskCompletion(task: DailyTask) {
        db.taskDao().updateTask(task.copy(isCompleted = !task.isCompleted))
    }

    suspend fun rescheduleTask(task: DailyTask, newStartTime: String, newEndTime: String) {
        db.taskDao().updateTask(task.copy(startTime = newStartTime, endTime = newEndTime))
    }

    // Goals & Milestones
    val allGoals: Flow<List<Goal>> = db.goalDao().getAllGoals()

    fun getMilestonesForGoal(goalId: Long): Flow<List<GoalMilestone>> = db.goalDao().getMilestonesForGoal(goalId)

    val allMilestones: Flow<List<GoalMilestone>> = db.goalDao().getAllMilestones()

    suspend fun addGoal(goal: Goal): Long = db.goalDao().insertGoal(goal)

    suspend fun updateGoal(goal: Goal) = db.goalDao().updateGoal(goal)

    suspend fun deleteGoal(goal: Goal) = db.goalDao().deleteGoal(goal)

    suspend fun addMilestone(milestone: GoalMilestone): Long = db.goalDao().insertMilestone(milestone)

    suspend fun toggleMilestoneCompletion(milestone: GoalMilestone) {
        db.goalDao().updateMilestone(milestone.copy(isCompleted = !milestone.isCompleted))
        // Recalculate goal progress
        val allMilestones = db.goalDao().getMilestonesForGoalOnce(milestone.goalId)
        if (allMilestones.isNotEmpty()) {
            val completed = allMilestones.count { if (it.id == milestone.id) !milestone.isCompleted else it.isCompleted }
            val progress = (completed * 100) / allMilestones.size
            val goal = db.goalDao().getGoalById(milestone.goalId)
            if (goal != null) {
                db.goalDao().updateGoal(goal.copy(progressPercentage = progress))
            }
        }
    }

    // Habits
    val allHabits: Flow<List<Habit>> = db.habitDao().getAllHabits()

    fun getHabitLogsForDate(date: String): Flow<List<HabitLog>> = db.habitDao().getHabitLogsForDate(date)

    suspend fun addHabit(habit: Habit): Long = db.habitDao().insertHabit(habit)

    suspend fun updateHabit(habit: Habit) = db.habitDao().updateHabit(habit)

    suspend fun deleteHabit(habit: Habit) = db.habitDao().deleteHabit(habit)

    suspend fun toggleHabitForDate(habit: Habit, date: String, currentlyDone: Boolean) {
        if (currentlyDone) {
            db.habitDao().deleteHabitLog(habit.id, date)
            val newStreak = (habit.currentStreak - 1).coerceAtLeast(0)
            db.habitDao().updateHabit(habit.copy(currentStreak = newStreak))
        } else {
            db.habitDao().insertHabitLog(HabitLog(habitId = habit.id, date = date, isCompleted = true))
            val newStreak = habit.currentStreak + 1
            val longest = maxOf(newStreak, habit.longestStreak)
            db.habitDao().updateHabit(habit.copy(currentStreak = newStreak, longestStreak = longest))
        }
    }

    // Study & Career
    val allTracks: Flow<List<String>> = db.studyDao().getAllTracks()

    fun getTopicsForTrack(track: String): Flow<List<StudyTopic>> = db.studyDao().getTopicsForTrack(track)

    val allTopics: Flow<List<StudyTopic>> = db.studyDao().getAllTopics()

    fun getReviewQueue(today: String): Flow<List<StudyTopic>> = db.studyDao().getReviewQueue(today)

    suspend fun updateTopic(topic: StudyTopic) = db.studyDao().updateTopic(topic)

    // Fitness & Nutrition
    val allWorkouts: Flow<List<WorkoutLog>> = db.fitnessDao().getAllWorkouts()

    suspend fun addWorkout(workout: WorkoutLog): Long = db.fitnessDao().insertWorkout(workout)

    fun getNutritionForDate(date: String): Flow<List<NutritionLog>> = db.fitnessDao().getNutritionForDate(date)

    suspend fun addNutrition(log: NutritionLog): Long = db.fitnessDao().insertNutrition(log)

    suspend fun deleteNutrition(log: NutritionLog) = db.fitnessDao().deleteNutrition(log)

    fun getWaterLogForDate(date: String): Flow<WaterLog?> = db.fitnessDao().getWaterLogForDate(date)

    suspend fun updateWaterGlasses(date: String, count: Int) {
        db.fitnessDao().insertOrUpdateWater(WaterLog(date = date, glassesCount = count))
    }

    // Journal
    val allJournalEntries: Flow<List<JournalEntry>> = db.journalDao().getAllEntries()

    val lessonsLearned: Flow<List<JournalEntry>> = db.journalDao().getLessonsLearned()

    suspend fun addJournalEntry(entry: JournalEntry): Long = db.journalDao().insertEntry(entry)

    suspend fun deleteJournalEntry(entry: JournalEntry) = db.journalDao().deleteEntry(entry)

    // Focus sessions
    fun getFocusSessionsForDate(date: String): Flow<List<FocusSession>> = db.focusDao().getSessionsForDate(date)

    suspend fun addFocusSession(session: FocusSession): Long = db.focusDao().insertSession(session)

    // Persistent Chat Messages
    val allChatMessages: Flow<List<com.example.data.local.ChatMessageEntity>> = db.chatDao().getAllMessages()

    suspend fun clearChatHistory() = db.chatDao().clearHistory()

    // AI Coach
    suspend fun getAiCoachAdvice(
        prompt: String,
        role: String = "LIFE_COACH",
        actionType: String = "GENERAL"
    ): String {
        val profile = db.userDao().getUserProfileOnce()
        val today = SeedData.getTodayString()
        val tasks = db.taskDao().getTasksForDateOnce(today)
        val habits = db.habitDao().getAllHabitsOnce()
        val history = db.chatDao().getAllMessagesOnce()

        // Persist User Message
        db.chatDao().insertMessage(
            com.example.data.local.ChatMessageEntity(
                sender = "USER",
                message = prompt,
                role = role
            )
        )

        val response = aiService.generateMultiTurnResponse(
            history = history,
            newPrompt = prompt,
            userProfile = profile,
            currentTasks = tasks,
            habits = habits,
            role = role,
            actionType = actionType
        )

        // Persist AI Response
        val modelUsed = when {
            role == "SCIENCE_SPECIALIST" || actionType in listOf("TEACH_ME", "INTERVIEW_SIMULATOR") -> "gemini-3.1-pro-preview"
            role == "RAPID_ASSISTANT" || actionType in listOf("WHAT_NEXT") -> "gemini-3.1-flash-lite-preview"
            else -> "gemini-3.5-flash"
        }
        db.chatDao().insertMessage(
            com.example.data.local.ChatMessageEntity(
                sender = "AI",
                message = response,
                modelUsed = modelUsed,
                role = role
            )
        )

        return response
    }

    // Data Export & Reset
    suspend fun exportDataAsJson(): String {
        val profile = db.userDao().getUserProfileOnce()
        val tasks = db.taskDao().getAllRecentTasks()
        val habits = db.habitDao().getAllHabitsOnce()
        val topics = db.studyDao().getAllTopicsOnce()
        val workouts = db.fitnessDao().getRecentWorkoutsOnce()
        val journals = db.journalDao().getRecentEntriesOnce()

        val root = JSONObject().apply {
            put("app", "Mohoraga - LEVEL UP OS")
            put("exportTimestamp", System.currentTimeMillis())
            put("profile", JSONObject().apply {
                profile?.let {
                    put("name", it.name)
                    put("age", it.age)
                    put("branch", it.branch)
                    put("careerGoal", it.primaryCareerGoal)
                    put("weightKg", it.currentWeightKg)
                    put("targetWeightKg", it.targetWeightKg)
                }
            })
            put("tasksCount", tasks.size)
            put("habitsCount", habits.size)
            put("topicsCount", topics.size)
            put("workoutsCount", workouts.size)
            put("journalsCount", journals.size)
        }
        return root.toString(2)
    }

    suspend fun resetAllDataToDefaults() {
        val today = SeedData.getTodayString()
        db.taskDao().deleteTasksForDate(today)
        db.userDao().insertOrUpdateProfile(SeedData.defaultProfile)
        db.taskDao().insertAll(SeedData.getInitialTasks(today))
        SeedData.initialGoals.forEach { db.goalDao().insertGoal(it) }
        SeedData.initialMilestones.forEach { db.goalDao().insertMilestone(it) }
        SeedData.initialHabits.forEach { db.habitDao().insertHabit(it) }
        db.studyDao().insertAllTopics(SeedData.initialStudyTopics)
    }
}
