package com.example.yoldeznouiraproject.data.parser

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class QuestionXmlParserTest {

    private lateinit var parser: QuestionXmlParser
    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        parser = QuestionXmlParser(context)
    }

    @Test
    fun `parseQuestionsFromXml returns list of questions`() {
        val questions = parser.parseQuestionsFromXml()
        
        assertThat(questions).isNotEmpty()
        assertThat(questions[0].monumentName).isNotEmpty()
        assertThat(questions[0].options).hasSize(4)
        assertThat(questions[0].fact).isNotEmpty()
    }

    @Test
    fun `parsed questions have correct data structure`() {
        val questions = parser.parseQuestionsFromXml()
        val firstQuestion = questions[0]
        
        assertThat(firstQuestion.correctLocation).isIn(firstQuestion.options)
    }
}
