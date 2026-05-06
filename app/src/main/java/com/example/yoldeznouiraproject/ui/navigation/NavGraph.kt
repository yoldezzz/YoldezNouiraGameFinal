package com.example.yoldeznouiraproject.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.yoldeznouiraproject.ui.screens.*
import com.example.yoldeznouiraproject.viewmodel.QuizEvent
import com.example.yoldeznouiraproject.viewmodel.QuizViewModel
import com.example.yoldeznouiraproject.viewmodel.SettingsViewModel

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Menu : Screen("menu")
    object Categories : Screen("categories")
    object Settings : Screen("settings")
    object Quiz : Screen("quiz")
    object Result : Screen("result")
}

@Composable
fun AppNavGraph(
    quizViewModel: QuizViewModel = viewModel(),
    settingsViewModel: SettingsViewModel = viewModel()
) {
    val navController = rememberNavController()
    val settingsState by settingsViewModel.uiState.collectAsState()
    val quizState by quizViewModel.uiState.collectAsState()
    val context = LocalContext.current

    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) {
            SplashScreen(onTimeout = { navController.navigate(Screen.Menu.route) })
        }

        composable(Screen.Menu.route) {
            MenuScreen(
                onStartGame = { navController.navigate(Screen.Categories.route) },
                onCategories = { navController.navigate(Screen.Categories.route) },  // same as Start Game
                onSettings = { navController.navigate(Screen.Settings.route) },
                onExit = { android.os.Process.killProcess(android.os.Process.myPid()) }
            )
        }

        composable(Screen.Categories.route) {
            CategoryScreen(
                onCategorySelected = { category ->
                    settingsViewModel.setCategory(category)
                    navController.navigate(Screen.Settings.route)
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                settingsState,
                { settingsViewModel.toggleTimer() },
                { settingsViewModel.setDifficulty(it) },
                {
                    // Apply settings to QuizViewModel
                    quizViewModel.onEvent(QuizEvent.SetDifficulty(settingsState.selectedDifficulty))
                    quizViewModel.onEvent(QuizEvent.SetTimerEnabled(settingsState.isTimerEnabled))
                    quizViewModel.onEvent(QuizEvent.LoadQuestions(settingsState.selectedCategory, context))
                    navController.navigate(Screen.Quiz.route)
                },
                { navController.popBackStack() }
            )
        }

        composable(Screen.Quiz.route) {
            QuizScreen(
                quizState = quizState,
                onAnswerSelected = { answer -> quizViewModel.onEvent(QuizEvent.SelectAnswer(answer)) },
                onNextQuestion = { quizViewModel.onEvent(QuizEvent.NextQuestion) },
                onRestart = { quizViewModel.onEvent(QuizEvent.RestartQuiz) }
            )
            if (quizState.isGameFinished) {
                LaunchedEffect(quizState.isGameFinished) {
                    navController.navigate(Screen.Result.route) {
                        popUpTo(Screen.Quiz.route) { inclusive = true }
                    }
                }
            }
        }

        composable(Screen.Result.route) {
            ResultScreen(
                score = quizState.score,
                totalQuestions = quizState.questions.size,
                answeredQuestions = quizState.answeredQuestions,
                onPlayAgain = {
                    quizViewModel.onEvent(QuizEvent.RestartQuiz)
                    navController.navigate(Screen.Quiz.route)
                },
                onBackToMenu = { navController.popBackStack(Screen.Menu.route, false) }
            )
        }
    }
}