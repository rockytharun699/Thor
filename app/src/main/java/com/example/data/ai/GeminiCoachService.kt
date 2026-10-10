package com.example.data.ai

import com.example.BuildConfig
import com.example.data.local.ChatMessageEntity
import com.example.data.local.DailyTask
import com.example.data.local.Habit
import com.example.data.local.UserProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class GeminiCoachService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    suspend fun generateMultiTurnResponse(
        history: List<ChatMessageEntity>,
        newPrompt: String,
        userProfile: UserProfile?,
        currentTasks: List<DailyTask>,
        habits: List<Habit>,
        role: String = "LIFE_COACH", // "LIFE_COACH", "SCIENCE_SPECIALIST", "RAPID_ASSISTANT"
        actionType: String = "GENERAL"
    ): String = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Throwable) {
            ""
        }

        // Model selection rule:
        // Complex STEM, math, science, deep architecture -> gemini-3.1-pro-preview
        // Fast actions, quick next move -> gemini-3.1-flash-lite-preview
        // General tasks -> gemini-3.5-flash
        val model = when {
            role == "SCIENCE_SPECIALIST" || actionType in listOf("TEACH_ME", "INTERVIEW_SIMULATOR") -> "gemini-3.1-pro-preview"
            role == "RAPID_ASSISTANT" || actionType in listOf("WHAT_NEXT") -> "gemini-3.1-flash-lite-preview"
            else -> "gemini-3.5-flash"
        }

        val profileSummary = if (userProfile != null) {
            """
            User Profile:
            - Name: ${userProfile.name}, Age: ${userProfile.age}
            - Education: ${userProfile.education}, Branch: ${userProfile.branch}
            - Primary Career Goal: ${userProfile.primaryCareerGoal}
            - Skill Level: ${userProfile.currentSkillLevel}
            - Exam: ${userProfile.examGoal}
            - Communication Goal: ${userProfile.communicationGoal}
            - Fitness: ${userProfile.fitnessGoal} (Current: ${userProfile.currentWeightKg}kg, Target: ${userProfile.targetWeightKg}kg)
            - Schedule: Wake at ${userProfile.targetWakeTime}, Sleep at ${userProfile.targetBedTime}
            - Challenge: ${userProfile.currentChallenge}
            """.trimIndent()
        } else {
            "User: Final-year B.Tech CSE student preparing for AI/ML engineering role, GATE DA, and muscle gain."
        }

        val completedTasksCount = currentTasks.count { it.isCompleted }
        val pendingTasks = currentTasks.filter { !it.isCompleted }.joinToString("\n") {
            "- [${it.priority}] ${it.startTime}-${it.endTime}: ${it.title} (${it.category})"
        }

        val roleInstruction = when (role) {
            "SCIENCE_SPECIALIST" -> """
                You are the Chief AI Scientist & STEM Engineering Professor in Mohoraga Life OS.
                You specialize in deep mathematical reasoning, Python scientific computing, statistics for data science, machine learning architectures, and GATE DA problem solving.
                Derive equations clearly, break down scientific principles intuitively, and provide exact, rigorous code and analytical explanations.
            """.trimIndent()

            "RAPID_ASSISTANT" -> """
                You are a high-speed tactical execution assistant in Mohoraga Life OS.
                Keep responses razor-sharp, immediate, bulleted, and under 100 words. Tell the user exactly what single action to execute right now.
            """.trimIndent()

            else -> """
                You are the personal AI Coach for 'Mohoraga - LEVEL UP Personal Life OS'.
                You act as a senior software engineer, AI product architect, fitness coach, and disciplined mentor.
                Respond with high empathy, clear structure, and ruthless realism.
                Prioritize high-impact deep work, adequate sleep (minimum 7.5 hours), and proper nutrition.
            """.trimIndent()
        }

        val fullSystemInstruction = """
            $roleInstruction
            $profileSummary
            Today's Progress: $completedTasksCount/${currentTasks.size} tasks done.
            Pending Tasks:
            $pendingTasks
        """.trimIndent()

        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext getIntelligentFallback(actionType, userProfile, currentTasks, newPrompt)
        }

        try {
            val url = "https://generativelanguage.googleapis.com/v1beta/models/$model:generateContent?key=$apiKey"

            val rootJson = JSONObject().apply {
                // systemInstruction
                put("systemInstruction", JSONObject().apply {
                    val parts = JSONArray()
                    parts.put(JSONObject().apply { put("text", fullSystemInstruction) })
                    put("parts", parts)
                })

                // Multi-turn contents array
                val contentsArray = JSONArray()

                // Append up to last 10 messages for conversation continuity
                val recentHistory = history.takeLast(10)
                recentHistory.forEach { chat ->
                    val turnObj = JSONObject().apply {
                        put("role", if (chat.sender == "USER") "user" else "model")
                        val parts = JSONArray()
                        parts.put(JSONObject().apply { put("text", chat.message) })
                        put("parts", parts)
                    }
                    contentsArray.put(turnObj)
                }

                // Add current user prompt
                val currentUserTurn = JSONObject().apply {
                    put("role", "user")
                    val parts = JSONArray()
                    parts.put(JSONObject().apply { put("text", newPrompt) })
                    put("parts", parts)
                }
                contentsArray.put(currentUserTurn)
                put("contents", contentsArray)

                val genConfig = JSONObject().apply {
                    put("temperature", if (role == "SCIENCE_SPECIALIST") 0.3 else 0.7)
                    put("topP", 0.95)
                    put("maxOutputTokens", 1500)
                }
                put("generationConfig", genConfig)
            }

            val requestBody = rootJson.toString().toRequestBody(jsonMediaType)
            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                return@withContext getIntelligentFallback(actionType, userProfile, currentTasks, newPrompt)
            }

            val responseBody = response.body?.string() ?: ""
            val jsonResponse = JSONObject(responseBody)
            val candidates = jsonResponse.optJSONArray("candidates")
            val firstCandidate = candidates?.optJSONObject(0)
            val content = firstCandidate?.optJSONObject("content")
            val parts = content?.optJSONArray("parts")
            val text = parts?.optJSONObject(0)?.optString("text")

            if (!text.isNullOrBlank()) {
                text.trim()
            } else {
                getIntelligentFallback(actionType, userProfile, currentTasks, newPrompt)
            }
        } catch (e: Exception) {
            getIntelligentFallback(actionType, userProfile, currentTasks, newPrompt)
        }
    }

    private fun getIntelligentFallback(
        actionType: String,
        profile: UserProfile?,
        tasks: List<DailyTask>,
        prompt: String
    ): String {
        val completed = tasks.count { it.isCompleted }
        val pending = tasks.filter { !it.isCompleted }
        val name = profile?.name ?: "Engineer"

        return when (actionType) {
            "PLAN_DAY" -> """
                🎯 **Optimized Daily Blueprint for $name**
                
                • **Primary Objective**: Advance Movie Recommendation Engine hybrid scoring and finish GATE DA probability questions.
                • **Morning Anchor (08:00 - 10:30)**: Hydrate, 35g protein fuel, then dive straight into 90 min deep work before phone notifications scatter your focus.
                • **Afternoon Focus (03:00 - 05:00)**: Implement cosine similarity vector search on your movie metadata.
                • **Evening Recovery (06:30 - 09:00)**: Upper body hypertrophy workout followed by your high-protein dinner (~45g protein).
                • **Night Wind-Down (11:00 - 12:30)**: Review today's logs, reflect for 10 minutes, and power down screens by 12:30 AM to safeguard hormonal recovery and brain plasticity.
            """.trimIndent()

            "REBUILD_SCHEDULE" -> """
                ⚡ **Schedule Recalibration (Zero Guilt Mode)**
                
                You have ${pending.size} pending tasks. Instead of cramming and burning out:
                
                1. **Non-Negotiable Core (Keep)**:
                   - AI/ML Project (Movie Recommender) - 60 focused minutes.
                   - Hypertrophy Workout & Dinner (Muscle synthesis cannot be skipped).
                2. **Consolidate**: Combine English speaking practice into a 15-minute aloud summary of today's ML concepts.
                3. **Postpone**: Shift lower-priority items to tomorrow's buffer block.
                4. **Protect Bedtime**: Sleep at 12:30 AM remains strict. Recovery is where muscle and memory consolidate.
            """.trimIndent()

            "WHAT_NEXT" -> {
                val nextTask = pending.firstOrNull()
                if (nextTask != null) {
                    """
                    🚀 **Your Immediate Next Move**
                    
                    👉 **${nextTask.title}** (${nextTask.category})
                    • Scheduled window: ${nextTask.startTime} - ${nextTask.endTime} (${nextTask.durationMinutes} min)
                    • Why it matters: This moves the needle on your ${nextTask.priority} priority target.
                    • Action: Close all extraneous browser tabs, turn phone face down on silent, and run a 25-minute Pomodoro sprint.
                    """.trimIndent()
                } else {
                    """
                    🎉 **All Scheduled Tasks Completed!**
                    
                    Outstanding execution today ($completed/${tasks.size} done).
                    • Recommended move: Head to the **Journal** tab for your evening 5-question reflection.
                    • Prepare your sleep sanctuary and aim to be in bed before 12:30 AM.
                    """.trimIndent()
                }
            }

            "FIND_WEAKNESSES" -> """
                🔍 **System Weakness & Friction Analysis**
                
                Based on your saved profile and logs:
                
                1. **Consistency Friction**: Transitioning between college coursework and evening self-study often creates decision fatigue. *Fix: Use a 10-minute mental buffer with hydration instead of social media checking.*
                2. **Technical Mastery**: Deep learning backpropagation & mathematical intuition need active testing rather than passive reading. *Fix: Solve 5 GATE questions by hand each day.*
                3. **Nutrition Gap**: Reaching 135g protein requires deliberate meal timing. Ensure you log each meal immediately after eating.
            """.trimIndent()

            "INTERVIEW_SIMULATOR" -> """
                🎙️ **AI Engineer Mock Interview Question**
                
                **Question**:
                "In your hybrid movie recommendation system, how do you handle the cold-start problem for a new user who has never rated a movie? Explain both content-based fallback and heuristic exploration."
                
                💡 **Key points to hit in your answer**:
                1. Differentiate user cold-start vs item cold-start.
                2. Fallback to demographic popularity or genre-based onboarding survey.
                3. Content-based embeddings for newly added movies using metadata (director, cast, synopsis TF-IDF/BERT).
                4. Multi-armed bandit / epsilon-greedy exploration to rapidly learn preferences.
            """.trimIndent()

            "ENGLISH_PRACTICE" -> """
                🗣️ **Spoken English Coaching Feedback**
                
                **Model Answer Structure**:
                "When designing our recommendation pipeline, we faced severe matrix sparsity. To mitigate this, I developed a hybrid architecture that weights collaborative matrix factorization alongside cosine similarity from text embeddings."
                
                **Vocabulary Upgrades**:
                • Instead of saying *"It was hard to fix"*, say *"It presented a significant engineering bottleneck"*.
                • Instead of *"The model was slow"*, say *"Inference latency exceeded our 50ms SLA, prompting vector indexing with FAISS"*.
            """.trimIndent()

            else -> """
                🤖 **Mohoraga AI Guidance**
                
                $prompt
                
                • **Key Insight**: Success in both AI engineering and muscle hypertrophy follows the law of progressive overload and disciplined iteration.
                • **Immediate Recommendation**: Focus on the single highest-priority task right now. Action cures anxiety.
            """.trimIndent()
        }
    }
}
