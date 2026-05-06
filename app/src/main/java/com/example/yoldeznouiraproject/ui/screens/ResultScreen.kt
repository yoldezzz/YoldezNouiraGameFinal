package com.example.yoldeznouiraproject.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yoldeznouiraproject.ui.theme.*
import com.example.yoldeznouiraproject.viewmodel.AnsweredQuestion
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    score: Int,
    totalQuestions: Int,
    answeredQuestions: List<AnsweredQuestion> = emptyList(),
    onPlayAgain: () -> Unit,
    onBackToMenu: () -> Unit
) {
    val maxScore = (totalQuestions * 10).coerceAtLeast(1)
    val progress = (score.toFloat() / maxScore).coerceIn(0f, 1f)
    val percentage = progress * 100
    val message = when {
        percentage >= 80 -> "🏆 Excellent! Outstanding performance!"
        percentage >= 60 -> "🌟 Great work! Keep improving!"
        percentage >= 40 -> "💪 Good effort! You'll master this!"
        else -> "📚 Keep learning — you'll improve fast!"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quiz Results", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MediterraneanSeaBlue,
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MediterraneanWarmCream)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top)
        ) {
            item {
                // Score Summary Card
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn() + slideInVertically() + scaleIn()
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateContentSize(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(4.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "Quiz Complete!",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = MediterraneanDarkSeaBlue
                            )

                            Box(contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(
                                    progress = { progress },
                                    strokeWidth = 12.dp,
                                    color = MediterraneanTerracotta,
                                    trackColor = MediterraneanTerracotta.copy(alpha = 0.2f),
                                    modifier = Modifier.size(150.dp),
                                    gapSize = 0.dp
                                )
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Text(
                                        text = "${percentage.roundToInt()}%",
                                        fontSize = 32.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MediterraneanTerracotta
                                    )
                                    Text(
                                        text = "$score / $maxScore",
                                        fontSize = 16.sp,
                                        color = MediterraneanSeaBlue,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }

                            Text(
                                text = message,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MediterraneanDarkSeaBlue,
                                modifier = Modifier
                                    .background(
                                        MediterraneanSunYellow.copy(alpha = 0.2f),
                                        RoundedCornerShape(8.dp)
                                    )
                                    .padding(12.dp)
                            )
                        }
                    }
                }
            }

            item {
                Text(
                    "Your Answers",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MediterraneanDarkSeaBlue,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            itemsIndexed(answeredQuestions) { index, answer ->
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(initialAlpha = 0f) + slideInVertically(initialOffsetY = { 50 }),
                    modifier = Modifier.animateContentSize()
                ) {
                    AnswerCard(
                        question = answer,
                        questionNumber = index + 1,
                        delay = index * 50
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onPlayAgain,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MediterraneanTerracotta
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Play Again", fontWeight = FontWeight.Bold)
                    }
                    OutlinedButton(
                        onClick = onBackToMenu,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            width = 2.dp
                        ),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MediterraneanSeaBlue
                        )
                    ) {
                        Text("Menu", fontWeight = FontWeight.Bold)
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun AnswerCard(
    question: AnsweredQuestion,
    questionNumber: Int,
    delay: Int
) {
    val backgroundColor = if (question.isCorrect) {
        Color(0xFFE8F5E9)
    } else {
        Color(0xFFFFF3E0)
    }

    val statusEmoji = if (question.isCorrect) "✓" else "✗"
    val statusColor = if (question.isCorrect) CorrectAnswerGreen else IncorrectAnswerRed

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Question $questionNumber",
                    fontSize = 14.sp,
                    color = MediterraneanSeaBlue,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = statusEmoji,
                    fontSize = 24.sp,
                    color = statusColor
                )
            }

            Text(
                text = question.monumentName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MediterraneanDarkSeaBlue
            )

            if (!question.isCorrect) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color.White.copy(alpha = 0.5f),
                                RoundedCornerShape(6.dp)
                            )
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Your answer:",
                            fontSize = 13.sp,
                            color = MediterraneanSeaBlue
                        )
                        Text(
                            text = question.selectedAnswer ?: "No answer (Timeout)",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = IncorrectAnswerRed
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                CorrectAnswerGreen.copy(alpha = 0.1f),
                                RoundedCornerShape(6.dp)
                            )
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Correct answer:",
                            fontSize = 13.sp,
                            color = MediterraneanSeaBlue
                        )
                        Text(
                            text = question.correctLocation,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = CorrectAnswerGreen
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MediterraneanSunYellow.copy(alpha = 0.15f),
                        RoundedCornerShape(8.dp)
                    )
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "💡 Did you know?",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MediterraneanDarkSeaBlue
                )
                Text(
                    text = question.fact,
                    fontSize = 12.sp,
                    color = MediterraneanSeaBlue,
                    lineHeight = 16.sp
                )
            }
        }
    }
}
