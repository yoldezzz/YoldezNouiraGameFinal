package com.example.yoldeznouiraproject

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.yoldeznouiraproject.viewmodel.QuizEvent
import com.example.yoldeznouiraproject.viewmodel.QuizViewModel
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class QuizViewModelTest {

    private lateinit var viewModel: QuizViewModel
    private lateinit var context: Context

    @Before
    fun setup() {
        viewModel = QuizViewModel()
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun `loadQuestions updates state with questions`() {
        viewModel.onEvent(QuizEvent.LoadQuestions("roman", context))
        
        val state = viewModel.uiState.value
        assertThat(state.questions).isNotEmpty()
        assertThat(state.currentQuestionIndex).isEqualTo(0)
    }

    @Test
    fun `selecting correct answer increases score`() {
        viewModel.onEvent(QuizEvent.LoadQuestions("roman", context))
        val initialQuestion = viewModel.uiState.value.questions[0]
        val correctAnswer = initialQuestion.correctLocation

        viewModel.onEvent(QuizEvent.SelectAnswer(correctAnswer))

        val state = viewModel.uiState.value
        assertThat(state.score).isEqualTo(10)
        assertThat(state.answered).isTrue()
    }

    @Test
    fun `selecting wrong answer does not increase score`() {
        viewModel.onEvent(QuizEvent.LoadQuestions("roman", context))
        val initialQuestion = viewModel.uiState.value.questions[0]
        val wrongAnswer = initialQuestion.options.first { it != initialQuestion.correctLocation }

        viewModel.onEvent(QuizEvent.SelectAnswer(wrongAnswer))

        val state = viewModel.uiState.value
        assertThat(state.score).isEqualTo(0)
        assertThat(state.answered).isTrue()
    }

    @Test
    fun `nextQuestion increments index`() {
        viewModel.onEvent(QuizEvent.LoadQuestions("roman", context))
        viewModel.onEvent(QuizEvent.SelectAnswer("Some Answer"))
        
        viewModel.onEvent(QuizEvent.NextQuestion)

        val state = viewModel.uiState.value
        assertThat(state.currentQuestionIndex).isEqualTo(1)
        assertThat(state.answered).isFalse()
    }
}
