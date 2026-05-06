package com.example.yoldeznouiraproject.data.parser

import android.content.Context
import com.example.yoldeznouiraproject.R
import com.example.yoldeznouiraproject.data.model.Difficulty
import com.example.yoldeznouiraproject.data.model.Question
import org.xmlpull.v1.XmlPullParser

class QuestionXmlParser(private val context: Context) {

    fun parseQuestionsFromXml(): List<Question> {
        val questions = mutableListOf<Question>()
        val parser = context.resources.getXml(R.xml.roman_questions)

        try {
            var eventType = parser.eventType
            var currentMonumentName = ""
            var currentCorrectLocation = ""
            var currentId = 0
            var currentDifficulty = Difficulty.MEDIUM
            var currentOptions = mutableListOf<String>()
            var currentFact = ""

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    when (parser.name) {
                        "question" -> {
                            currentId = parser.getAttributeValue(null, "id")?.toIntOrNull() ?: 0
                            currentMonumentName = parser.getAttributeValue(null, "monumentName") ?: ""
                            currentCorrectLocation = parser.getAttributeValue(null, "correctLocation") ?: ""
                            val diffStr = parser.getAttributeValue(null, "difficulty")
                            currentDifficulty = when (diffStr) {
                                "EASY" -> Difficulty.EASY
                                "HARD" -> Difficulty.HARD
                                else -> Difficulty.MEDIUM
                            }
                            currentOptions = mutableListOf()
                            currentFact = ""
                        }
                        "option" -> {
                            currentOptions.add(parser.nextText().trim())
                        }
                        "fact" -> {
                            currentFact = parser.nextText().trim()
                        }
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    if (parser.name == "question") {
                        questions.add(
                            Question(
                                id = currentId,
                                monumentName = currentMonumentName,
                                correctLocation = currentCorrectLocation,
                                options = currentOptions.toList(),
                                fact = currentFact,
                                difficulty = currentDifficulty
                            )
                        )
                    }
                }
                eventType = parser.next()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return questions
    }
}
