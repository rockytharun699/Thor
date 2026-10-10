package com.example.data.local

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object SeedData {
    fun getTodayString(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    val defaultProfile = UserProfile(
        id = 1,
        name = "Mohoraga",
        age = 21,
        education = "Final-year B.Tech student",
        branch = "CSE (Artificial Intelligence & Machine Learning)",
        primaryCareerGoal = "Become an AI/ML Engineer",
        currentSkillLevel = "Beginner-to-intermediate in Python and ML",
        examGoal = "GATE DA (Data Science & AI) Preparation",
        communicationGoal = "Improve spoken English, fluency, confidence & technical interview answers",
        fitnessGoal = "Gain muscle and build a stronger, athletic physique",
        heightCm = 173f,
        currentWeightKg = 65f,
        targetWeightKg = 72f,
        fitnessExperience = "Beginner",
        dietPreference = "Non-vegetarian",
        targetWakeTime = "08:00 AM",
        targetBedTime = "12:30 AM",
        dailyTargetCalorie = 2500,
        dailyTargetProteinGrams = 135,
        currentChallenge = "Building consistency, developing practical skills & managing multiple goals",
        onboardingCompleted = true
    )

    fun getInitialTasks(today: String = getTodayString()): List<DailyTask> = listOf(
        DailyTask(
            date = today,
            title = "Wake Up & Hydration Routine",
            category = "Morning routine",
            startTime = "08:00 AM",
            endTime = "08:30 AM",
            durationMinutes = 30,
            priority = "High",
            isCompleted = false,
            notes = "500ml water, light mobility stretching, plan overview",
            orderIndex = 1
        ),
        DailyTask(
            date = today,
            title = "High-Protein Breakfast & Daily Focus Setup",
            category = "Meals & hydration",
            startTime = "08:30 AM",
            endTime = "09:00 AM",
            durationMinutes = 30,
            priority = "Medium",
            isCompleted = false,
            notes = "3 boiled eggs, oats with milk & banana (~35g protein)",
            orderIndex = 2
        ),
        DailyTask(
            date = today,
            title = "Deep Work: GATE DA Math & Probability",
            category = "GATE preparation",
            startTime = "09:00 AM",
            endTime = "10:30 AM",
            durationMinutes = 90,
            priority = "Critical",
            isCompleted = false,
            notes = "Continuous random variables, Bayes theorem practice questions",
            orderIndex = 3
        ),
        DailyTask(
            date = today,
            title = "College Lectures & Lab Session",
            category = "College",
            startTime = "10:45 AM",
            endTime = "02:00 PM",
            durationMinutes = 195,
            priority = "Medium",
            isCompleted = false,
            notes = "Final-year coursework and distributed systems lab",
            orderIndex = 4
        ),
        DailyTask(
            date = today,
            title = "Nutritious Lunch & Mental Reset",
            category = "Meals & hydration",
            startTime = "02:00 PM",
            endTime = "03:00 PM",
            durationMinutes = 60,
            priority = "Medium",
            isCompleted = false,
            notes = "Chicken curry with rice, curd, and green salad (~40g protein)",
            orderIndex = 5
        ),
        DailyTask(
            date = today,
            title = "AI/ML Project: Hybrid Movie Recommendation Engine",
            category = "Project development",
            startTime = "03:00 PM",
            endTime = "05:00 PM",
            durationMinutes = 120,
            priority = "Critical",
            isCompleted = false,
            notes = "Combine collaborative filtering with TF-IDF cosine similarity embeddings",
            orderIndex = 6
        ),
        DailyTask(
            date = today,
            title = "Spoken English & Technical Interview Drill",
            category = "English communication",
            startTime = "05:15 PM",
            endTime = "06:00 PM",
            durationMinutes = 45,
            priority = "High",
            isCompleted = false,
            notes = "Practice explaining gradient descent and regularization aloud in clear English",
            orderIndex = 7
        ),
        DailyTask(
            date = today,
            title = "Gym Session: Hypertrophy Upper Body (Push/Pull)",
            category = "Workout",
            startTime = "06:30 PM",
            endTime = "07:45 PM",
            durationMinutes = 75,
            priority = "High",
            isCompleted = false,
            notes = "Dumbbell bench press, Lat pulldowns, Overhead press, Barbell rows",
            orderIndex = 8
        ),
        DailyTask(
            date = today,
            title = "Post-Workout Dinner & Recovery Shake",
            category = "Meals & hydration",
            startTime = "08:15 PM",
            endTime = "09:00 PM",
            durationMinutes = 45,
            priority = "High",
            isCompleted = false,
            notes = "Paneer/chicken wrap + 1 scoop whey protein with water (~45g protein)",
            orderIndex = 9
        ),
        DailyTask(
            date = today,
            title = "ML Theory & Spaced Repetition Flashcards",
            category = "AI/ML learning",
            startTime = "09:15 PM",
            endTime = "10:45 PM",
            durationMinutes = 90,
            priority = "High",
            isCompleted = false,
            notes = "Review bias-variance tradeoff and XGBoost split algorithms",
            orderIndex = 10
        ),
        DailyTask(
            date = today,
            title = "Evening Reflection, Journal & Wind Down",
            category = "Evening review",
            startTime = "11:00 PM",
            endTime = "11:45 PM",
            durationMinutes = 45,
            priority = "Medium",
            isCompleted = false,
            notes = "Answer 5 daily reflection questions, log wins, prep tomorrow's goals",
            orderIndex = 11
        ),
        DailyTask(
            date = today,
            title = "Screen Off & Sleep Preparation",
            category = "Sleep preparation",
            startTime = "11:45 PM",
            endTime = "12:30 AM",
            durationMinutes = 45,
            priority = "High",
            isCompleted = false,
            notes = "Dark environment, read physical book, target sleep by 12:30 AM",
            orderIndex = 12
        )
    )

    val initialGoals = listOf(
        Goal(
            id = 1,
            title = "Become Job-Ready AI/ML Engineer",
            description = "Land an entry-level AI/ML engineer or data scientist role with strong practical fundamentals.",
            reason = "Launch high-impact engineering career, build intelligent software systems, achieve financial independence.",
            category = "Career",
            targetDate = "2027-05-30",
            priority = "Critical",
            progressPercentage = 35,
            successCriteria = "2 production-ready deployed ML projects on GitHub, passed 5 mock interviews, solid system design portfolio.",
            status = "IN_PROGRESS"
        ),
        Goal(
            id = 2,
            title = "Deploy Hybrid Movie Recommendation System",
            description = "Complete and host a fully functioning recommendation service combining collaborative filtering and content-based embedding retrieval.",
            reason = "Showcase end-to-end ML engineering: data pipelines, model evaluation, FastAPI backend, and cloud containerization.",
            category = "Projects",
            targetDate = "2026-11-20",
            priority = "High",
            progressPercentage = 50,
            successCriteria = "FastAPI endpoints returning Top-N recommendations with <50ms latency, Dockerized and deployed with interactive UI.",
            status = "IN_PROGRESS"
        ),
        Goal(
            id = 3,
            title = "Systematic GATE DA Exam Preparation",
            description = "Master syllabus: Probability, Linear Algebra, Calculus, ML, AI, DBMS, Programming & Data Structures.",
            reason = "Build world-class foundational rigor for AI research, M.Tech admissions or prestigious PSU opportunities.",
            category = "GATE",
            targetDate = "2027-02-05",
            priority = "High",
            progressPercentage = 28,
            successCriteria = "Complete 5 full-length mock tests with >70 percentile score and thorough error log analysis.",
            status = "IN_PROGRESS"
        ),
        Goal(
            id = 4,
            title = "Fluent & Confident Spoken English in Interviews",
            description = "Eliminate hesitation, speak clearly with structured articulation (STAR method) in technical & behavioral interviews.",
            reason = "Transform strong technical aptitude into confident communication that interviewers respect.",
            category = "Communication",
            targetDate = "2026-12-15",
            priority = "High",
            progressPercentage = 40,
            successCriteria = "Comfortably answer technical ML questions for 30 minutes without stalling or anxiety.",
            status = "IN_PROGRESS"
        ),
        Goal(
            id = 5,
            title = "Gain 7 kg Lean Muscle (65kg -> 72kg)",
            description = "Follow progressive overload weight training 4x/week and hit 2,500 kcal + 135g protein consistently.",
            reason = "Build high physical energy, confidence, posture, and a strong athletic physique.",
            category = "Fitness",
            targetDate = "2027-04-30",
            priority = "High",
            progressPercentage = 25,
            successCriteria = "Reach 72 kg body weight with visible shoulder, chest, and back development; bench press 1x bodyweight.",
            status = "IN_PROGRESS"
        ),
        Goal(
            id = 6,
            title = "Unbreakable Personal Discipline & Focus",
            description = "Achieve 85%+ weekly habit consistency, eliminate mindless social media doomscrolling, respect sleep schedule.",
            reason = "Discipline is the engine that converts ambition into actual compound mastery.",
            category = "Personal",
            targetDate = "2026-12-31",
            priority = "Medium",
            progressPercentage = 45,
            successCriteria = "Sleep by 12:30 AM 6 days a week, daily deep work uninterrupted for at least 2 hours.",
            status = "IN_PROGRESS"
        )
    )

    val initialMilestones = listOf(
        GoalMilestone(goalId = 1, title = "Master Python typing, OOP, and NumPy vectorization", isCompleted = true, targetDate = "2026-10-25"),
        GoalMilestone(goalId = 1, title = "Complete Scikit-Learn end-to-end model pipeline with cross-validation", isCompleted = false, targetDate = "2026-11-15"),
        GoalMilestone(goalId = 1, title = "Implement deep neural network and fine-tune Transformer embedding model", isCompleted = false, targetDate = "2026-12-20"),
        GoalMilestone(goalId = 1, title = "Build FastAPI microservice with Docker and deploy to Cloud", isCompleted = false, targetDate = "2027-01-15"),
        
        GoalMilestone(goalId = 2, title = "Clean MovieLens dataset and build TF-IDF metadata matrix", isCompleted = true, targetDate = "2026-10-18"),
        GoalMilestone(goalId = 2, title = "Train matrix factorization (SVD / ALS) collaborative filtering model", isCompleted = true, targetDate = "2026-10-28"),
        GoalMilestone(goalId = 2, title = "Write hybrid weighted score blending content and collaborative scores", isCompleted = false, targetDate = "2026-11-10"),
        GoalMilestone(goalId = 2, title = "Deploy interactive web API with latency benchmarking", isCompleted = false, targetDate = "2026-11-20"),

        GoalMilestone(goalId = 3, title = "Complete Linear Algebra (Eigenvalues, SVD, Vector spaces)", isCompleted = true, targetDate = "2026-10-30"),
        GoalMilestone(goalId = 3, title = "Solve 200 Probability & Statistics GATE questions", isCompleted = false, targetDate = "2026-11-30"),
        GoalMilestone(goalId = 3, title = "Revise Machine Learning & AI algorithms for GATE DA format", isCompleted = false, targetDate = "2026-12-30"),

        GoalMilestone(goalId = 4, title = "Record 20 daily 3-minute technical spoken answers", isCompleted = true, targetDate = "2026-10-31"),
        GoalMilestone(goalId = 4, title = "Complete 5 mock AI engineer interviews on AI Tutor", isCompleted = false, targetDate = "2026-11-25"),

        GoalMilestone(goalId = 5, title = "Consistently hit 130g+ protein for 30 consecutive days", isCompleted = false, targetDate = "2026-11-10"),
        GoalMilestone(goalId = 5, title = "Reach 67 kg scale weight with progressive overload on compounds", isCompleted = false, targetDate = "2026-12-05")
    )

    val initialHabits = listOf(
        Habit(id = 1, title = "Wake Up by 08:00 AM", category = "Discipline", frequency = "DAILY", targetPerWeek = 7, currentStreak = 4, longestStreak = 9, reminderTime = "08:00 AM", iconName = "alarm"),
        Habit(id = 2, title = "90 Min Deep Work Session", category = "Productivity", frequency = "DAILY", targetPerWeek = 6, currentStreak = 5, longestStreak = 12, reminderTime = "09:00 AM", iconName = "laptop"),
        Habit(id = 3, title = "ML Code / Math Practice", category = "Learning", frequency = "DAILY", targetPerWeek = 6, currentStreak = 6, longestStreak = 14, reminderTime = "03:00 PM", iconName = "terminal"),
        Habit(id = 4, title = "GATE DA Question Drill", category = "Learning", frequency = "DAILY", targetPerWeek = 5, currentStreak = 3, longestStreak = 8, reminderTime = "05:00 PM", iconName = "quiz"),
        Habit(id = 5, title = "Spoken English Vocalization", category = "Communication", frequency = "DAILY", targetPerWeek = 6, currentStreak = 4, longestStreak = 10, reminderTime = "06:00 PM", iconName = "mic"),
        Habit(id = 6, title = "Hypertrophy Workout (4x/wk)", category = "Fitness", frequency = "4_TIMES_A_WEEK", targetPerWeek = 4, currentStreak = 2, longestStreak = 7, reminderTime = "06:30 PM", iconName = "fitness"),
        Habit(id = 7, title = "Hit 135g Protein Target", category = "Nutrition", frequency = "DAILY", targetPerWeek = 7, currentStreak = 5, longestStreak = 11, reminderTime = "09:00 PM", iconName = "restaurant"),
        Habit(id = 8, title = "Evening Reflection & Journal", category = "Mindset", frequency = "DAILY", targetPerWeek = 7, currentStreak = 6, longestStreak = 15, reminderTime = "11:00 PM", iconName = "book"),
        Habit(id = 9, title = "Screen Off by 12:30 AM", category = "Health", frequency = "DAILY", targetPerWeek = 7, currentStreak = 3, longestStreak = 8, reminderTime = "12:15 AM", iconName = "bedtime")
    )

    val initialStudyTopics = listOf(
        StudyTopic(
            id = 1,
            track = "Python Programming",
            title = "Decorators & Closures in Python",
            explanation = "A closure is a function object that remembers values in enclosing scopes even if they are not present in memory. A decorator is a callable that takes a function as argument and returns an augmented function using `@syntax`.",
            prerequisites = "First-class functions, *args and **kwargs",
            learningObjectives = "Build timing decorators, caching/memoization wrappers, and API authentication decorators.",
            practicalExercise = "Write a `@timer_decorator` that measures execution time of a matrix multiplication function using `time.perf_counter()`.",
            quizQuestion = "What happens if a decorator does not use `functools.wraps(fn)` on the wrapper function?",
            quizOptionsJson = "[\"The code throws a SyntaxError\", \"The original function loses its __name__ and docstring metadata\", \"The function executes twice\", \"It causes memory leak in the Python interpreter\"]",
            correctOptionIndex = 1,
            interviewQuestions = "Q: What is the difference between a function decorator and a class decorator? How does functools.lru_cache work internally?",
            masteryScore = 85,
            status = "MASTERED",
            lastReviewedDate = "2026-10-08",
            nextReviewDueDate = "2026-10-15"
        ),
        StudyTopic(
            id = 2,
            track = "NumPy & Pandas",
            title = "Broadcasting & Vectorized Operations",
            explanation = "NumPy arrays allow element-wise mathematical operations at C-speed without Python loops. Broadcasting describes how NumPy treats arrays with different shapes during arithmetic operations under compatible trailing dimension rules.",
            prerequisites = "NumPy ndarray basics, array shapes and dtypes",
            learningObjectives = "Avoid slow `for` loops in numerical data processing; understand shape alignment rules.",
            practicalExercise = "Normalize a 2D matrix (samples x features) by subtracting column means and dividing by standard deviations using vectorized operations.",
            quizQuestion = "Can an array of shape (4, 1) broadcast with an array of shape (4, 5)?",
            quizOptionsJson = "[\"Yes, the dimension with size 1 stretches to 5\", \"No, both arrays must have identical shapes\", \"Only if axis=0 is explicitly passed\", \"Yes, but it converts all values to zeroes\"]",
            correctOptionIndex = 0,
            interviewQuestions = "Q: Why is vectorized NumPy code drastically faster than a standard Python list comprehension? (Hint: GIL, memory locality, SIMD instructions).",
            masteryScore = 70,
            status = "IN_PROGRESS",
            lastReviewedDate = "2026-10-09",
            nextReviewDueDate = "2026-10-12"
        ),
        StudyTopic(
            id = 3,
            track = "Mathematics & Statistics",
            title = "Eigenvalues, Eigenvectors & PCA",
            explanation = "For a linear transformation matrix A, a non-zero vector v is an eigenvector if Av = λv, where λ is the eigenvalue. Principal Component Analysis (PCA) uses eigenvectors of the covariance matrix to project data onto orthogonal axes of maximum variance.",
            prerequisites = "Matrix multiplication, determinants, variance & covariance",
            learningObjectives = "Calculate characteristic equations, derive principal components, and apply dimensionality reduction.",
            practicalExercise = "Compute the 2x2 covariance matrix of a dataset, find its eigenvalues, and determine the percentage of variance retained by the first principal component.",
            quizQuestion = "In PCA, what do the eigenvectors of the covariance matrix represent?",
            quizOptionsJson = "[\"The mean of each feature\", \"The directions of the principal axes of maximal variance\", \"The reconstruction error of the model\", \"The cluster centroids of k-means\"]",
            correctOptionIndex = 1,
            interviewQuestions = "Q: How do you choose the number of principal components in practice? What is the relationship between SVD and PCA?",
            masteryScore = 65,
            status = "IN_PROGRESS",
            lastReviewedDate = "2026-10-05",
            nextReviewDueDate = "2026-10-11"
        ),
        StudyTopic(
            id = 4,
            track = "Supervised ML",
            title = "Bias-Variance Tradeoff & Regularization (L1/L2)",
            explanation = "High bias causes underfitting (model is too simple). High variance causes overfitting (model captures noise). Regularization penalizes model complexity: L1 (Lasso) uses |w| producing sparse weights, while L2 (Ridge) uses w² shrinking weights toward zero.",
            prerequisites = "Linear Regression, Cost functions, Gradient descent",
            learningObjectives = "Diagnose high bias vs high variance from train/validation curves; choose between L1 and L2 regularization.",
            practicalExercise = "Plot learning curves for polynomial regressions of degree 1, 3, and 15; compare MSE on unseen test data.",
            quizQuestion = "Why does L1 regularization (Lasso) induce feature sparsity (zeroing out weights) while L2 (Ridge) does not?",
            quizOptionsJson = "[\"Because L1 has a diamond-shaped constraint boundary with sharp corners at axes\", \"Because L1 multiplies weights by zero\", \"Because L2 has no derivative at zero\", \"Because L1 only works on categorical variables\"]",
            correctOptionIndex = 0,
            interviewQuestions = "Q: If your model has high training accuracy (99%) but poor test accuracy (65%), what steps do you take to improve it?",
            masteryScore = 80,
            status = "MASTERED",
            lastReviewedDate = "2026-10-07",
            nextReviewDueDate = "2026-10-14"
        ),
        StudyTopic(
            id = 5,
            track = "Ensemble Learning",
            title = "Random Forests vs Gradient Boosting & XGBoost",
            explanation = "Random Forests use Bagging (Bootstrap Aggregating) to train independent deep trees in parallel, reducing variance. Gradient Boosting trains shallow trees sequentially, each fitting the residual errors (pseudo-residuals) of the previous trees to reduce bias.",
            prerequisites = "Decision trees, Gini impurity, entropy, residuals",
            learningObjectives = "Tune hyperparameters (n_estimators, max_depth, learning_rate, subsample); explain why XGBoost is fast.",
            practicalExercise = "Train a Random Forest and an XGBoost Classifier on an imbalanced classification dataset and compare Precision-Recall AUC.",
            quizQuestion = "In Gradient Boosting, what does each new decision tree fit to?",
            quizOptionsJson = "[\"A bootstrap sample of original targets\", \"The negative gradient of the loss function (residuals)\", \"The random average of all previous trees\", \"The dominant principal component\"]",
            correctOptionIndex = 1,
            interviewQuestions = "Q: How does XGBoost handle missing values during training and inference? What makes XGBoost faster than standard GBM?",
            masteryScore = 55,
            status = "IN_PROGRESS",
            lastReviewedDate = "2026-10-04",
            nextReviewDueDate = "2026-10-11"
        ),
        StudyTopic(
            id = 6,
            track = "Deep Learning",
            title = "Backpropagation & Optimizers (SGD, Adam)",
            explanation = "Backpropagation uses the calculus chain rule to compute gradients of the loss with respect to every weight in a multi-layer network. Adam (Adaptive Moment Estimation) combines momentum (first moment of gradients) and RMSprop (second moment / variance scaling).",
            prerequisites = "Multivariable calculus (partial derivatives), Matrix multiplication, Cross-entropy loss",
            learningObjectives = "Derive gradient flow through a hidden layer; explain vanishing/exploding gradients and why Adam converges faster.",
            practicalExercise = "Implement a 2-layer neural network with ReLU and Softmax from scratch in pure NumPy without PyTorch.",
            quizQuestion = "What is the primary benefit of the Adam optimizer over standard Stochastic Gradient Descent (SGD)?",
            quizOptionsJson = "[\"It eliminates the need for activation functions\", \"It computes individual adaptive learning rates for each parameter using momentum and squared gradients\", \"It guarantees finding the global minimum of non-convex functions\", \"It runs without requiring backward passes\"]",
            correctOptionIndex = 1,
            interviewQuestions = "Q: Explain vanishing gradients with the Sigmoid activation function and how ReLU and residual connections (ResNet) resolve this.",
            masteryScore = 40,
            status = "TODO",
            lastReviewedDate = "",
            nextReviewDueDate = "2026-10-10"
        ),
        StudyTopic(
            id = 7,
            track = "NLP & RAG",
            title = "Text Embeddings & Vector Search (RAG Pipelines)",
            explanation = "Embeddings map semantic text meanings into dense high-dimensional vectors (e.g., 768 or 1536 dims). Retrieval-Augmented Generation (RAG) chunks documents, generates embeddings, queries a vector index via cosine similarity, and injects relevant chunks into the LLM prompt.",
            prerequisites = "Cosine similarity, tokenization, transformer basics",
            learningObjectives = "Design a RAG system with chunking strategies, vector database retrieval, and re-ranking.",
            practicalExercise = "Build a Python script that chunks a 10-page PDF, retrieves top-3 relevant paragraphs for a question using cosine distance, and generates an answer.",
            quizQuestion = "What is the formula for cosine similarity between two embedding vectors A and B?",
            quizOptionsJson = "[\"(A · B) / (||A|| * ||B||)\", \"||A - B||²\", \"A · B + ||A|| + ||B||\", \"sqrt(sum((A_i - B_i)²))\"]",
            correctOptionIndex = 0,
            interviewQuestions = "Q: What are the common failure modes of naive RAG, and how do chunk overlap, hybrid search (BM25 + Dense), and cross-encoder re-ranking mitigate them?",
            masteryScore = 50,
            status = "IN_PROGRESS",
            lastReviewedDate = "2026-10-06",
            nextReviewDueDate = "2026-10-12"
        ),
        StudyTopic(
            id = 8,
            track = "Deployment & APIs",
            title = "Production ML Serving with FastAPI & Docker",
            explanation = "Deploying ML requires serving predictions via high-throughput asynchronous REST APIs. FastAPI leverages Pydantic for request validation, Python type hints, and Starlette async loop. Docker packages Python dependencies, system libraries, and model weights into reproducible containers.",
            prerequisites = "HTTP methods (GET, POST), JSON serialization, Python virtual environments",
            learningObjectives = "Create a `/predict` POST endpoint, load model artifacts on app startup using lifespan context managers, and containerize.",
            practicalExercise = "Write a `main.py` FastAPI app with a Pydantic input schema, load a pickled scikit-learn model, and write a Dockerfile exposing port 8000.",
            quizQuestion = "Why should heavy ML model weights be loaded in FastAPI's startup/lifespan handler rather than inside the route function itself?",
            quizOptionsJson = "[\"Because route functions cannot access the filesystem\", \"To load the model into memory once and avoid massive disk I/O latency on every single request\", \"To prevent the model from overfitting\", \"Because Python crashes if models are in route functions\"]",
            correctOptionIndex = 1,
            interviewQuestions = "Q: How do you handle batching incoming inference requests in FastAPI to maximize GPU throughput?",
            masteryScore = 60,
            status = "IN_PROGRESS",
            lastReviewedDate = "2026-10-07",
            nextReviewDueDate = "2026-10-13"
        ),
        StudyTopic(
            id = 9,
            track = "GATE DA",
            title = "Conditional Probability & Bayes Theorem",
            explanation = "Bayes Theorem calculates posterior probability: P(A|B) = [P(B|A) * P(A)] / P(B). In GATE DA, questions test total probability law, independence, random variables, expectation E[X], and variance Var(X).",
            prerequisites = "Set theory, Venn diagrams, permutations & combinations",
            learningObjectives = "Solve complex probability word problems, calculate marginal distributions, and apply Bayes Rule under noisy sensor conditions.",
            practicalExercise = "Solve: A test for a rare disease is 99% accurate (both sensitivity and specificity). The prevalence of the disease in the population is 0.1%. What is the probability that a person who tests positive actually has the disease?",
            quizQuestion = "In the medical diagnosis problem above with 0.1% prevalence and 99% accuracy, what is approximately P(Disease | Positive)?",
            quizOptionsJson = "[\"99.0%\", \"~9.0%\", \"50.0%\", \"1.0%\"]",
            correctOptionIndex = 1,
            interviewQuestions = "Q: Derive Bayes Theorem from the definition of conditional probability. Explain why high accuracy does not imply high precision when base rates are tiny.",
            masteryScore = 75,
            status = "IN_PROGRESS",
            lastReviewedDate = "2026-10-08",
            nextReviewDueDate = "2026-10-11"
        ),
        StudyTopic(
            id = 10,
            track = "Interview Prep",
            title = "Explain Hybrid Recommendation Systems (Your Project)",
            explanation = "A hybrid recommendation system overcomes the cold-start problem and sparsity of collaborative filtering by combining user-item matrix factorization (collaborative) with item metadata feature embeddings (content-based).",
            prerequisites = "Cosine similarity, SVD, precision@K, recall@K",
            learningObjectives = "Articulate architecture choices, tradeoffs, latency constraints, and evaluation metrics in a technical interview with poise.",
            practicalExercise = "Give a 3-minute spoken explanation of your movie recommender covering: problem statement, data pipeline, hybrid scoring formula, and evaluation (RMSE & NDCG@10).",
            quizQuestion = "Which evaluation metric measures both relevance and the ranking order of recommended items?",
            quizOptionsJson = "[\"Mean Absolute Error (MAE)\", \"Normalized Discounted Cumulative Gain (NDCG@K)\", \"Accuracy\", \"Log-Loss\"]",
            correctOptionIndex = 1,
            interviewQuestions = "Q: How does your recommender handle a brand new user who has not rated any movies yet? How do you scale candidate generation vs ranking?",
            masteryScore = 70,
            status = "IN_PROGRESS",
            lastReviewedDate = "2026-10-09",
            nextReviewDueDate = "2026-10-12"
        )
    )
}
