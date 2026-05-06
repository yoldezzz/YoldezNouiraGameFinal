package com.example.yoldeznouiraproject.data.repository

import android.content.Context
import com.example.yoldeznouiraproject.data.model.Question
import com.example.yoldeznouiraproject.data.parser.QuestionXmlParser

class QuestionRepository(private val context: Context) {

    private val parser = QuestionXmlParser(context)

    fun getRomanQuestions(): List<Question> {
        return parser.parseQuestionsFromXml()
    }
}