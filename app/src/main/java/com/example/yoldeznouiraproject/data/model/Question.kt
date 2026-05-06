package com.example.yoldeznouiraproject.data.model

enum class Difficulty { EASY, MEDIUM, HARD }

data class Question(
    val id: Int,
    val monumentName: String,
    val correctLocation: String,
    val options: List<String>,
    val fact: String,
    val difficulty: Difficulty
)