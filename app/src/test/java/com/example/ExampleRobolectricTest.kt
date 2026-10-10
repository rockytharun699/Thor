package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.AppDatabase
import com.example.data.local.ChatMessageEntity
import com.example.data.local.DailyTask
import com.example.data.local.Goal
import com.example.data.local.HabitLog
import com.example.data.local.SeedData
import com.example.data.local.UserProfile
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  private lateinit var db: AppDatabase

  @Before
  fun setup() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
      .allowMainThreadQueries()
      .build()
  }

  @After
  fun teardown() {
    db.close()
  }

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Mohoraga", appName)
  }

  @Test
  fun `seed data verification`() = runTest {
    val profile = SeedData.defaultProfile
    assertEquals("Mohoraga", profile.name)
    assertEquals(21, profile.age)
    assertEquals("Become an AI/ML Engineer", profile.primaryCareerGoal)
    assertEquals(65f, profile.currentWeightKg)
    assertEquals(72f, profile.targetWeightKg)

    val tasks = SeedData.getInitialTasks()
    assertTrue(tasks.isNotEmpty())
    assertTrue(tasks.any { it.category == "GATE preparation" })
    assertTrue(tasks.any { it.category == "Project development" })
  }

  @Test
  fun `test full CRUD on UserProfileDao DailyTaskDao GoalDao and HabitLogDao`() = runTest {
    val userDao = db.userProfileDao()
    val taskDao = db.dailyTaskDao()
    val goalDao = db.goalDao()
    val habitLogDao = db.habitLogDao()

    // 1. UserProfileDao: Insert -> Query -> Update -> Delete
    val profile = UserProfile(id = 1, name = "Mohoraga CRUD", currentWeightKg = 65f, targetWeightKg = 72f)
    userDao.insertProfile(profile)
    val fetchedUser = userDao.getUserProfileById(1)
    assertNotNull(fetchedUser)
    assertEquals("Mohoraga CRUD", fetchedUser?.name)

    userDao.updateProfile(fetchedUser!!.copy(name = "Mohoraga Updated", currentWeightKg = 67f))
    val updatedUser = userDao.getUserProfileById(1)
    assertEquals("Mohoraga Updated", updatedUser?.name)
    assertEquals(67f, updatedUser?.currentWeightKg)

    userDao.deleteProfile(updatedUser!!)
    val deletedUser = userDao.getUserProfileById(1)
    assertNull(deletedUser)

    // 2. DailyTaskDao: Insert -> Query -> Update -> Delete
    val task = DailyTask(
      date = "2026-10-10",
      title = "Science Lab Python Matrix Drill",
      category = "AI/ML learning",
      startTime = "10:00 AM",
      endTime = "11:30 AM",
      durationMinutes = 90,
      priority = "Critical"
    )
    val taskId = taskDao.insertTask(task)
    val fetchedTask = taskDao.getTaskById(taskId)
    assertNotNull(fetchedTask)
    assertEquals("Science Lab Python Matrix Drill", fetchedTask?.title)

    taskDao.updateTask(fetchedTask!!.copy(isCompleted = true, title = "Matrix Drill Completed"))
    val updatedTask = taskDao.getTaskById(taskId)
    assertTrue(updatedTask!!.isCompleted)
    assertEquals("Matrix Drill Completed", updatedTask.title)

    taskDao.deleteTask(updatedTask)
    val deletedTask = taskDao.getTaskById(taskId)
    assertNull(deletedTask)

    // 3. GoalDao: Insert -> Query -> Update -> Delete
    val goal = Goal(
      title = "Master Machine Learning Theory",
      description = "Deep study of bias-variance and optimization",
      reason = "Engineering rigor",
      category = "Technical skills",
      targetDate = "2027-01-01",
      priority = "High",
      progressPercentage = 20,
      successCriteria = "Derive backprop by hand"
    )
    val goalId = goalDao.insertGoal(goal)
    val fetchedGoal = goalDao.getGoalById(goalId)
    assertNotNull(fetchedGoal)
    assertEquals("Master Machine Learning Theory", fetchedGoal?.title)

    goalDao.updateGoal(fetchedGoal!!.copy(progressPercentage = 50))
    val updatedGoal = goalDao.getGoalById(goalId)
    assertEquals(50, updatedGoal?.progressPercentage)

    goalDao.deleteGoal(updatedGoal!!)
    val deletedGoal = goalDao.getGoalById(goalId)
    assertNull(deletedGoal)

    // 4. HabitLogDao: Insert -> Query -> Update -> Delete
    val log = HabitLog(id = 100, habitId = 5, date = "2026-10-10", isCompleted = true)
    habitLogDao.insertHabitLog(log)
    val logs = habitLogDao.getHabitLogsForDateOnce("2026-10-10")
    assertTrue(logs.any { it.habitId == 5L })

    habitLogDao.updateHabitLog(log.copy(isCompleted = false))
    habitLogDao.deleteHabitLogEntity(log)
    val afterDeleteLogs = habitLogDao.getHabitLogsForDateOnce("2026-10-10")
    assertTrue(afterDeleteLogs.none { it.id == 100L })
  }
}
