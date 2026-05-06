package com.example.yoldeznouiraproject.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yoldeznouiraproject.data.model.Question
import com.example.yoldeznouiraproject.data.repository.QuestionRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AnsweredQuestion(
    val monumentName: String,
    val correctLocation: String,
    val selectedAnswer: String?,
    val fact: String,
    val isCorrect: Boolean
)

data class QuizUiState(
    val questions: List<Question> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val score: Int = 0,
    val answered: Boolean = false,
    val selectedOption: String? = null,
    val isGameFinished: Boolean = false,
    val timeRemaining: Int = 15,
    val isTimerEnabled: Boolean = true,
    val difficulty: String = "MEDIUM",
    val answeredQuestions: List<AnsweredQuestion> = emptyList()
)

sealed class QuizEvent {
    data class SelectAnswer(val answer: String) : QuizEvent()
    object NextQuestion : QuizEvent()
    object RestartQuiz : QuizEvent()
    data class SetTimerEnabled(val enabled: Boolean) : QuizEvent()
    data class SetDifficulty(val difficulty: String) : QuizEvent()
    data class TickTimer(val remaining: Int) : QuizEvent()
    data class LoadQuestions(val category: String, val context: Context) : QuizEvent()
}

class QuizViewModel : ViewModel() {

    private lateinit var repository: QuestionRepository
    private var timerJob: Job? = null

    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    fun onEvent(event: QuizEvent) {
        when (event) {
            is QuizEvent.SelectAnswer -> selectAnswer(event.answer)
            is QuizEvent.NextQuestion -> goToNextQuestion()
            is QuizEvent.RestartQuiz -> restartQuiz()
            is QuizEvent.SetTimerEnabled -> setTimerEnabled(event.enabled)
            is QuizEvent.SetDifficulty -> setDifficulty(event.difficulty)
            is QuizEvent.TickTimer -> updateTimer(event.remaining)
            is QuizEvent.LoadQuestions -> loadQuestions(event.category, event.context)
        }
    }

    private fun loadQuestions(category: String, context: Context) {
        stopTimer()
        repository = QuestionRepository(context)
        val questions = when (category.lowercase()) {
            "roman" -> repository.getRomanQuestions()
            else -> repository.getRomanQuestions()
        }
        _uiState.update {
            it.copy(
                questions = questions,
                currentQuestionIndex = 0,
                score = 0,
                answered = false,
                selectedOption = null,
                isGameFinished = false,
                timeRemaining = getTimeForDifficulty(),
                answeredQuestions = emptyList()
            )
        }
        if (questions.isNotEmpty()) {
            startTimer()
        }
    }

    private fun selectAnswer(answer: String) {
        if (_uiState.value.answered) return
        val currentQ = _uiState.value.questions.getOrNull(_uiState.value.currentQuestionIndex) ?: return
        val isCorrect = answer.equals(currentQ.correctLocation, ignoreCase = true)
        val newScore = if (isCorrect) _uiState.value.score + 10 else _uiState.value.score
        val answeredQ = AnsweredQuestion(
            monumentName = currentQ.monumentName,
            correctLocation = currentQ.correctLocation,
            selectedAnswer = answer,
            fact = currentQ.fact.ifBlank { "Tunisia is home to some of the best-preserved Roman sites in Africa." },
            isCorrect = isCorrect
        )
        _uiState.update {
            it.copy(
                selectedOption = answer,
                answered = true,
                score = newScore,
                answeredQuestions = it.answeredQuestions + answeredQ
            )
        }
        stopTimer()
    }

    private fun handleTimeout() {
        if (_uiState.value.answered || _uiState.value.isGameFinished) return
        val currentQ = _uiState.value.questions.getOrNull(_uiState.value.currentQuestionIndex) ?: return
        val newScore = _uiState.value.score
        val answeredQ = AnsweredQuestion(
            monumentName = currentQ.monumentName,
            correctLocation = currentQ.correctLocation,
            selectedAnswer = null,
            fact = currentQ.fact.ifBlank { "Tunisia is home to some of the best-preserved Roman sites in Africa." },
            isCorrect = false
        )
        _uiState.update {
            it.copy(
                selectedOption = "",
                answered = true,
                score = newScore,
                answeredQuestions = it.answeredQuestions + answeredQ
            )
        }
        stopTimer()
    }

    private fun goToNextQuestion() {
        stopTimer()
        val nextIndex = _uiState.value.currentQuestionIndex + 1
        if (nextIndex >= _uiState.value.questions.size) {
            _uiState.update {
                it.copy(
                    isGameFinished = true,
                    answered = false,
                    selectedOption = null
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    currentQuestionIndex = nextIndex,
                    answered = false,
                    selectedOption = null,
                    isGameFinished = false,
                    timeRemaining = getTimeForDifficulty()
                )
            }
            startTimer()
        }
    }

    private fun restartQuiz() {
        stopTimer()
        _uiState.update {
            it.copy(
                currentQuestionIndex = 0,
                score = 0,
                answered = false,
                selectedOption = null,
                isGameFinished = false,
                timeRemaining = getTimeForDifficulty(),
                answeredQuestions = emptyList()
            )
        }
        startTimer()
    }

    private fun setTimerEnabled(enabled: Boolean) {
        _uiState.update { it.copy(isTimerEnabled = enabled) }
        if (enabled && !_uiState.value.answered && !_uiState.value.isGameFinished) {
            startTimer()
        } else if (!enabled) {
            stopTimer()
            _uiState.update { it.copy(timeRemaining = getTimeForDifficulty()) }
        }
    }

    private fun setDifficulty(difficulty: String) {
        _uiState.update { it.copy(difficulty = difficulty, timeRemaining = getTimeForDifficulty()) }
        if (_uiState.value.isTimerEnabled && !_uiState.value.answered && !_uiState.value.isGameFinished) {
            startTimer()
        }
    }

    private fun getTimeForDifficulty(): Int {
        return when (_uiState.value.difficulty) {
            "EASY" -> 20
            "MEDIUM" -> 15
            "HARD" -> 10
            else -> 15
        }
    }

    private fun startTimer() {
        if (!_uiState.value.isTimerEnabled) return
        if (_uiState.value.answered || _uiState.value.isGameFinished) return
        stopTimer()
        timerJob = viewModelScope.launch {
            var remaining = _uiState.value.timeRemaining
            while (remaining > 0) {
                delay(1000)
                remaining--
                _uiState.update { it.copy(timeRemaining = remaining) }
                if (remaining == 0 && !_uiState.value.answered) {
                    handleTimeout()
                    break
                }
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    private fun updateTimer(remaining: Int) {
        _uiState.update { it.copy(timeRemaining = remaining) }
    }

    override fun onCleared() {
        super.onCleared()
        stopTimer()
    }
}
