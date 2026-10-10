package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1,
    val name: String = "Mohoraga",
    val age: Int = 21,
    val education: String = "Final-year B.Tech student",
    val branch: String = "CSE (Artificial Intelligence & Machine Learning)",
    val primaryCareerGoal: String = "Become an AI/ML Engineer",
    val currentSkillLevel: String = "Beginner-to-intermediate in Python and ML",
    val examGoal: String = "GATE DA (Data Science & AI) Preparation",
    val communicationGoal: String = "Improve spoken English fluency, confidence & interview answers",
    val fitnessGoal: String = "Gain muscle and build an athletic physique",
    val heightCm: Float = 173f,
    val currentWeightKg: Float = 65f,
    val targetWeightKg: Float = 72f,
    val fitnessExperience: String = "Beginner",
    val dietPreference: String = "Non-vegetarian",
    val targetWakeTime: String = "08:00 AM",
    val targetBedTime: String = "12:30 AM",
    val dailyTargetCalorie: Int = 2450,
    val dailyTargetProteinGrams: Int = 135,
    val currentChallenge: String = "Building consistency, practical skills & managing multiple goals",
    val onboardingCompleted: Boolean = true
)

@Entity(tableName = "daily_tasks")
data class DailyTask(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String, // YYYY-MM-DD
    val title: String,
    val category: String, // "Morning routine", "Deep work", "College", "GATE preparation", "AI/ML learning", "Project development", "English communication", "Workout", "Meals & hydration", "Evening review", etc.
    val startTime: String, // "09:00 AM"
    val endTime: String,   // "10:30 AM"
    val durationMinutes: Int,
    val priority: String, // "Critical", "High", "Medium", "Low"
    val isCompleted: Boolean = false,
    val notes: String = "",
    val orderIndex: Int = 0
)

@Entity(tableName = "goals")
data class Goal(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String,
    val reason: String,
    val category: String, // "Career", "Technical skills", "Projects", "GATE", "Communication", "Fitness", "Personal"
    val targetDate: String,
    val priority: String,
    val progressPercentage: Int = 0,
    val successCriteria: String,
    val status: String = "IN_PROGRESS" // IN_PROGRESS, COMPLETED, PAUSED
)

@Entity(tableName = "goal_milestones")
data class GoalMilestone(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val goalId: Long,
    val title: String,
    val targetDate: String = "",
    val isCompleted: Boolean = false
)

@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val category: String,
    val frequency: String = "DAILY", // DAILY, WEEKDAYS, 3_TIMES_A_WEEK
    val targetPerWeek: Int = 7,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val isPaused: Boolean = false,
    val reminderTime: String = "08:00 AM",
    val iconName: String = "check"
)

@Entity(tableName = "habit_logs")
data class HabitLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val habitId: Long,
    val date: String, // YYYY-MM-DD
    val isCompleted: Boolean = true
)

@Entity(tableName = "study_topics")
data class StudyTopic(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val track: String, // "Python Programming", "NumPy & Pandas", "Mathematics & Statistics", "Supervised ML", "Deep Learning", "NLP & RAG", "Deployment & APIs", "GATE DA", "Interview Prep"
    val title: String,
    val explanation: String,
    val prerequisites: String,
    val learningObjectives: String,
    val practicalExercise: String,
    val quizQuestion: String,
    val quizOptionsJson: String, // JSON array string
    val correctOptionIndex: Int,
    val interviewQuestions: String,
    val masteryScore: Int = 0, // 0-100
    val status: String = "TODO", // TODO, IN_PROGRESS, MASTERED
    val lastReviewedDate: String = "",
    val nextReviewDueDate: String = ""
)

@Entity(tableName = "workout_logs")
data class WorkoutLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String,
    val routineName: String,
    val durationMinutes: Int,
    val exerciseCount: Int,
    val totalVolumeKg: Float = 0f,
    val notes: String = "",
    val energyRating: Int = 4 // 1-5
)

@Entity(tableName = "nutrition_logs")
data class NutritionLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String,
    val mealName: String,
    val mealType: String, // Breakfast, Lunch, Snack, Dinner
    val calories: Int,
    val proteinGrams: Float,
    val carbsGrams: Float,
    val fatGrams: Float,
    val time: String = ""
)

@Entity(tableName = "water_logs")
data class WaterLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String,
    val glassesCount: Int = 0
)

@Entity(tableName = "journal_entries")
data class JournalEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String,
    val mood: String, // Great, Good, Focused, Neutral, Tired, Stressed
    val energyLevel: Int, // 1 to 5
    val feelingAnswer: String,
    val priorityAnswer: String,
    val distractionAnswer: String,
    val winAnswer: String,
    val improvementAnswer: String,
    val tags: String = "",
    val isLessonLearned: Boolean = false
)

@Entity(tableName = "focus_sessions")
data class FocusSession(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String,
    val durationMinutes: Int,
    val taskTitle: String,
    val category: String
)

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sender: String, // "USER" or "AI"
    val message: String,
    val modelUsed: String = "gemini-3.5-flash",
    val role: String = "Life OS Coach",
    val timestamp: Long = System.currentTimeMillis()
)

