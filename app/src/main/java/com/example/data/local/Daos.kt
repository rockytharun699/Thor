package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

// ==========================================
// 1. UserProfileDao (Full CRUD)
// ==========================================
@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    fun getUserProfile(): Flow<UserProfile?>

    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    suspend fun getUserProfileOnce(): UserProfile?

    @Query("SELECT * FROM user_profile WHERE id = :id LIMIT 1")
    suspend fun getUserProfileById(id: Int): UserProfile?

    @Query("SELECT * FROM user_profile")
    fun getAllProfiles(): Flow<List<UserProfile>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: UserProfile): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfile)

    @Update
    suspend fun updateProfile(profile: UserProfile)

    @Delete
    suspend fun deleteProfile(profile: UserProfile)

    @Query("DELETE FROM user_profile WHERE id = :id")
    suspend fun deleteProfileById(id: Int)
}

// Backward-compatibility alias
typealias UserDao = UserProfileDao

// ==========================================
// 2. DailyTaskDao (Full CRUD)
// ==========================================
@Dao
interface DailyTaskDao {
    @Query("SELECT * FROM daily_tasks WHERE date = :date ORDER BY orderIndex ASC, startTime ASC")
    fun getTasksForDate(date: String): Flow<List<DailyTask>>

    @Query("SELECT * FROM daily_tasks WHERE date = :date ORDER BY orderIndex ASC, startTime ASC")
    suspend fun getTasksForDateOnce(date: String): List<DailyTask>

    @Query("SELECT * FROM daily_tasks WHERE id = :id LIMIT 1")
    suspend fun getTaskById(id: Long): DailyTask?

    @Query("SELECT * FROM daily_tasks ORDER BY date DESC LIMIT 200")
    suspend fun getAllRecentTasks(): List<DailyTask>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: DailyTask): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tasks: List<DailyTask>)

    @Update
    suspend fun updateTask(task: DailyTask)

    @Delete
    suspend fun deleteTask(task: DailyTask)

    @Query("DELETE FROM daily_tasks WHERE date = :date")
    suspend fun deleteTasksForDate(date: String)

    @Query("DELETE FROM daily_tasks WHERE id = :id")
    suspend fun deleteTaskById(id: Long)
}

// Backward-compatibility alias
typealias TaskDao = DailyTaskDao

// ==========================================
// 3. GoalDao (Full CRUD)
// ==========================================
@Dao
interface GoalDao {
    @Query("SELECT * FROM goals ORDER BY priority DESC, id ASC")
    fun getAllGoals(): Flow<List<Goal>>

    @Query("SELECT * FROM goals WHERE id = :id LIMIT 1")
    suspend fun getGoalById(id: Long): Goal?

    @Query("SELECT * FROM goals WHERE category = :category ORDER BY id ASC")
    fun getGoalsByCategory(category: String): Flow<List<Goal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: Goal): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllGoals(goals: List<Goal>)

    @Update
    suspend fun updateGoal(goal: Goal)

    @Delete
    suspend fun deleteGoal(goal: Goal)

    @Query("DELETE FROM goals WHERE id = :id")
    suspend fun deleteGoalById(id: Long)

    // Milestones sub-queries
    @Query("SELECT * FROM goal_milestones WHERE goalId = :goalId")
    fun getMilestonesForGoal(goalId: Long): Flow<List<GoalMilestone>>

    @Query("SELECT * FROM goal_milestones WHERE goalId = :goalId")
    suspend fun getMilestonesForGoalOnce(goalId: Long): List<GoalMilestone>

    @Query("SELECT * FROM goal_milestones")
    fun getAllMilestones(): Flow<List<GoalMilestone>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMilestone(milestone: GoalMilestone): Long

    @Update
    suspend fun updateMilestone(milestone: GoalMilestone)

    @Delete
    suspend fun deleteMilestone(milestone: GoalMilestone)
}

// ==========================================
// 4. MilestoneDao (Dedicated Entity CRUD)
// ==========================================
@Dao
interface MilestoneDao {
    @Query("SELECT * FROM goal_milestones WHERE goalId = :goalId")
    fun getMilestonesForGoal(goalId: Long): Flow<List<GoalMilestone>>

    @Query("SELECT * FROM goal_milestones WHERE goalId = :goalId")
    suspend fun getMilestonesForGoalOnce(goalId: Long): List<GoalMilestone>

    @Query("SELECT * FROM goal_milestones")
    fun getAllMilestones(): Flow<List<GoalMilestone>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMilestone(milestone: GoalMilestone): Long

    @Update
    suspend fun updateMilestone(milestone: GoalMilestone)

    @Delete
    suspend fun deleteMilestone(milestone: GoalMilestone)

    @Query("DELETE FROM goal_milestones WHERE id = :id")
    suspend fun deleteMilestoneById(id: Long)
}

// ==========================================
// 5. HabitDao (Full CRUD)
// ==========================================
@Dao
interface HabitDao {
    @Query("SELECT * FROM habits ORDER BY id ASC")
    fun getAllHabits(): Flow<List<Habit>>

    @Query("SELECT * FROM habits")
    suspend fun getAllHabitsOnce(): List<Habit>

    @Query("SELECT * FROM habits WHERE id = :id LIMIT 1")
    suspend fun getHabitById(id: Long): Habit?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: Habit): Long

    @Update
    suspend fun updateHabit(habit: Habit)

    @Delete
    suspend fun deleteHabit(habit: Habit)

    @Query("DELETE FROM habits WHERE id = :id")
    suspend fun deleteHabitById(id: Long)

    @Query("SELECT * FROM habit_logs WHERE date = :date")
    fun getHabitLogsForDate(date: String): Flow<List<HabitLog>>

    @Query("SELECT * FROM habit_logs WHERE date = :date")
    suspend fun getHabitLogsForDateOnce(date: String): List<HabitLog>

    @Query("SELECT * FROM habit_logs WHERE habitId = :habitId")
    suspend fun getLogsForHabit(habitId: Long): List<HabitLog>

    @Query("SELECT * FROM habit_logs ORDER BY date DESC LIMIT 500")
    suspend fun getAllRecentHabitLogs(): List<HabitLog>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabitLog(log: HabitLog)

    @Query("DELETE FROM habit_logs WHERE habitId = :habitId AND date = :date")
    suspend fun deleteHabitLog(habitId: Long, date: String)
}

// ==========================================
// 6. HabitLogDao (Full CRUD)
// ==========================================
@Dao
interface HabitLogDao {
    @Query("SELECT * FROM habit_logs WHERE date = :date")
    fun getHabitLogsForDate(date: String): Flow<List<HabitLog>>

    @Query("SELECT * FROM habit_logs WHERE date = :date")
    suspend fun getHabitLogsForDateOnce(date: String): List<HabitLog>

    @Query("SELECT * FROM habit_logs WHERE habitId = :habitId")
    suspend fun getLogsForHabit(habitId: Long): List<HabitLog>

    @Query("SELECT * FROM habit_logs ORDER BY date DESC LIMIT 500")
    suspend fun getAllRecentHabitLogs(): List<HabitLog>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabitLog(log: HabitLog): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(logs: List<HabitLog>)

    @Update
    suspend fun updateHabitLog(log: HabitLog)

    @Delete
    suspend fun deleteHabitLogEntity(log: HabitLog)

    @Query("DELETE FROM habit_logs WHERE habitId = :habitId AND date = :date")
    suspend fun deleteHabitLog(habitId: Long, date: String)

    @Query("DELETE FROM habit_logs WHERE id = :id")
    suspend fun deleteHabitLogById(id: Long)
}

// ==========================================
// 7. StudyTopicDao (StudyDao)
// ==========================================
@Dao
interface StudyDao {
    @Query("SELECT DISTINCT track FROM study_topics")
    fun getAllTracks(): Flow<List<String>>

    @Query("SELECT * FROM study_topics WHERE track = :track ORDER BY id ASC")
    fun getTopicsForTrack(track: String): Flow<List<StudyTopic>>

    @Query("SELECT * FROM study_topics ORDER BY id ASC")
    fun getAllTopics(): Flow<List<StudyTopic>>

    @Query("SELECT * FROM study_topics ORDER BY id ASC")
    suspend fun getAllTopicsOnce(): List<StudyTopic>

    @Query("SELECT * FROM study_topics WHERE status != 'MASTERED' OR nextReviewDueDate <= :todayDate ORDER BY nextReviewDueDate ASC LIMIT 5")
    fun getReviewQueue(todayDate: String): Flow<List<StudyTopic>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTopic(topic: StudyTopic): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTopics(topics: List<StudyTopic>)

    @Update
    suspend fun updateTopic(topic: StudyTopic)

    @Delete
    suspend fun deleteTopic(topic: StudyTopic)
}

// ==========================================
// 8. WorkoutDao (WorkoutLog CRUD)
// ==========================================
@Dao
interface WorkoutDao {
    @Query("SELECT * FROM workout_logs ORDER BY date DESC")
    fun getAllWorkouts(): Flow<List<WorkoutLog>>

    @Query("SELECT * FROM workout_logs ORDER BY date DESC LIMIT 30")
    suspend fun getRecentWorkoutsOnce(): List<WorkoutLog>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workout: WorkoutLog): Long

    @Update
    suspend fun updateWorkout(workout: WorkoutLog)

    @Delete
    suspend fun deleteWorkout(workout: WorkoutLog)

    @Query("DELETE FROM workout_logs WHERE id = :id")
    suspend fun deleteWorkoutById(id: Long)
}

// ==========================================
// 9. NutritionDao (NutritionLog CRUD)
// ==========================================
@Dao
interface NutritionDao {
    @Query("SELECT * FROM nutrition_logs WHERE date = :date ORDER BY id ASC")
    fun getNutritionForDate(date: String): Flow<List<NutritionLog>>

    @Query("SELECT * FROM nutrition_logs ORDER BY date DESC LIMIT 200")
    suspend fun getAllNutritionOnce(): List<NutritionLog>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNutrition(log: NutritionLog): Long

    @Update
    suspend fun updateNutrition(log: NutritionLog)

    @Delete
    suspend fun deleteNutrition(log: NutritionLog)

    @Query("DELETE FROM nutrition_logs WHERE id = :id")
    suspend fun deleteNutritionById(id: Long)
}

// ==========================================
// 10. WaterDao (WaterLog CRUD)
// ==========================================
@Dao
interface WaterDao {
    @Query("SELECT * FROM water_logs WHERE date = :date LIMIT 1")
    fun getWaterLogForDate(date: String): Flow<WaterLog?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateWater(log: WaterLog)

    @Update
    suspend fun updateWater(log: WaterLog)

    @Delete
    suspend fun deleteWater(log: WaterLog)
}

// Combined Fitness DAO
@Dao
interface FitnessDao {
    @Query("SELECT * FROM workout_logs ORDER BY date DESC")
    fun getAllWorkouts(): Flow<List<WorkoutLog>>

    @Query("SELECT * FROM workout_logs ORDER BY date DESC LIMIT 30")
    suspend fun getRecentWorkoutsOnce(): List<WorkoutLog>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workout: WorkoutLog): Long

    @Query("SELECT * FROM nutrition_logs WHERE date = :date ORDER BY id ASC")
    fun getNutritionForDate(date: String): Flow<List<NutritionLog>>

    @Query("SELECT * FROM nutrition_logs ORDER BY date DESC LIMIT 200")
    suspend fun getAllNutritionOnce(): List<NutritionLog>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNutrition(log: NutritionLog): Long

    @Delete
    suspend fun deleteNutrition(log: NutritionLog)

    @Query("SELECT * FROM water_logs WHERE date = :date LIMIT 1")
    fun getWaterLogForDate(date: String): Flow<WaterLog?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateWater(log: WaterLog)
}

// ==========================================
// 11. JournalDao (JournalEntry CRUD)
// ==========================================
@Dao
interface JournalDao {
    @Query("SELECT * FROM journal_entries ORDER BY date DESC, id DESC")
    fun getAllEntries(): Flow<List<JournalEntry>>

    @Query("SELECT * FROM journal_entries WHERE isLessonLearned = 1 ORDER BY date DESC")
    fun getLessonsLearned(): Flow<List<JournalEntry>>

    @Query("SELECT * FROM journal_entries WHERE date = :date LIMIT 1")
    suspend fun getEntryForDate(date: String): JournalEntry?

    @Query("SELECT * FROM journal_entries ORDER BY date DESC LIMIT 30")
    suspend fun getRecentEntriesOnce(): List<JournalEntry>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: JournalEntry): Long

    @Update
    suspend fun updateEntry(entry: JournalEntry)

    @Delete
    suspend fun deleteEntry(entry: JournalEntry)

    @Query("DELETE FROM journal_entries WHERE id = :id")
    suspend fun deleteEntryById(id: Long)
}

// ==========================================
// 12. FocusDao (FocusSession CRUD)
// ==========================================
@Dao
interface FocusDao {
    @Query("SELECT * FROM focus_sessions WHERE date = :date")
    fun getSessionsForDate(date: String): Flow<List<FocusSession>>

    @Query("SELECT * FROM focus_sessions ORDER BY date DESC LIMIT 100")
    suspend fun getAllSessions(): List<FocusSession>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: FocusSession): Long

    @Update
    suspend fun updateSession(session: FocusSession)

    @Delete
    suspend fun deleteSession(session: FocusSession)

    @Query("DELETE FROM focus_sessions WHERE id = :id")
    suspend fun deleteSessionById(id: Long)
}

// ==========================================
// 13. ChatDao (ChatMessage CRUD)
// ==========================================
@Dao
interface ChatDao {
    @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC")
    fun getAllMessages(): Flow<List<ChatMessageEntity>>

    @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC")
    suspend fun getAllMessagesOnce(): List<ChatMessageEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessageEntity): Long

    @Update
    suspend fun updateMessage(message: ChatMessageEntity)

    @Delete
    suspend fun deleteMessage(message: ChatMessageEntity)

    @Query("DELETE FROM chat_messages")
    suspend fun clearHistory()
}
