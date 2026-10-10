package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserProfile::class,
        DailyTask::class,
        Goal::class,
        GoalMilestone::class,
        Habit::class,
        HabitLog::class,
        StudyTopic::class,
        WorkoutLog::class,
        NutritionLog::class,
        WaterLog::class,
        JournalEntry::class,
        FocusSession::class,
        ChatMessageEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    // DAOs for each entity
    abstract fun userProfileDao(): UserProfileDao
    fun userDao(): UserDao = userProfileDao()
    abstract fun dailyTaskDao(): DailyTaskDao
    fun taskDao(): TaskDao = dailyTaskDao()
    abstract fun goalDao(): GoalDao
    abstract fun milestoneDao(): MilestoneDao
    abstract fun habitDao(): HabitDao
    abstract fun habitLogDao(): HabitLogDao
    abstract fun studyDao(): StudyDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun nutritionDao(): NutritionDao
    abstract fun waterDao(): WaterDao
    abstract fun fitnessDao(): FitnessDao
    abstract fun journalDao(): JournalDao
    abstract fun focusDao(): FocusDao
    abstract fun chatDao(): ChatDao

    companion object {
        // Volatile ensures that multiple threads handle the INSTANCE variable correctly
        @Volatile
        private var INSTANCE: AppDatabase? = null

        const val DATABASE_NAME = "mohoraga_os.db"

        /**
         * Returns the singleton instance of AppDatabase using double-checked locking.
         */
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                .fallbackToDestructiveMigration(true)
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Seed database in background coroutine on first create
                        CoroutineScope(Dispatchers.IO).launch {
                            val database = getInstance(context)
                            seedDatabase(database)
                        }
                    }

                    override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
                        super.onDestructiveMigration(db)
                        // Re-seed database after destructive schema migration
                        CoroutineScope(Dispatchers.IO).launch {
                            val database = getInstance(context)
                            seedDatabase(database)
                        }
                    }
                })
                .build()
                INSTANCE = instance
                instance
            }
        }

        /**
         * Prepopulates initial data when the database is first initialized or reset.
         */
        suspend fun seedDatabase(db: AppDatabase) {
            val user = db.userDao().getUserProfileOnce()
            if (user == null) {
                db.userDao().insertOrUpdateProfile(SeedData.defaultProfile)
            }
            val tasks = db.taskDao().getTasksForDateOnce(SeedData.getTodayString())
            if (tasks.isEmpty()) {
                db.taskDao().insertAll(SeedData.getInitialTasks())
            }
            val goals = db.goalDao().getGoalById(1)
            if (goals == null) {
                SeedData.initialGoals.forEach { goal ->
                    db.goalDao().insertGoal(goal)
                }
                SeedData.initialMilestones.forEach { milestone ->
                    db.goalDao().insertMilestone(milestone)
                }
            }
            val habits = db.habitDao().getAllHabitsOnce()
            if (habits.isEmpty()) {
                SeedData.initialHabits.forEach { habit ->
                    db.habitDao().insertHabit(habit)
                }
            }
            val topics = db.studyDao().getAllTopicsOnce()
            if (topics.isEmpty()) {
                db.studyDao().insertAllTopics(SeedData.initialStudyTopics)
            }
            val chatHistory = db.chatDao().getAllMessagesOnce()
            if (chatHistory.isEmpty()) {
                db.chatDao().insertMessage(
                    ChatMessageEntity(
                        sender = "AI",
                        message = "Welcome back, Mohoraga. I have synchronized your daily targets: AI/ML engineering roadmap, GATE DA probability drills, 2,500 kcal muscle gain targets, and 12:30 AM sleep boundary. Select a role above (Life OS Coach, Science & STEM Specialist, or Rapid Tactical Assistant) and let's execute.",
                        modelUsed = "gemini-3.5-flash",
                        role = "LIFE_COACH"
                    )
                )
            }
        }
    }
}
