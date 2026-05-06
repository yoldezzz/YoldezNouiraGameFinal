package com.example.yoldeznouiraproject.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yoldeznouiraproject.R
import com.example.yoldeznouiraproject.ui.theme.*
import com.example.yoldeznouiraproject.viewmodel.QuizUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    quizState: QuizUiState,
    onAnswerSelected: (String) -> Unit,
    onNextQuestion: () -> Unit,
    onRestart: () -> Unit
) {
    if (quizState.questions.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(color = MediterraneanSeaBlue)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Loading Heritage Sites...", color = MediterraneanSeaBlue)
            }
        }
        return
    }

    val currentQuestion = quizState.questions.getOrNull(quizState.currentQuestionIndex) ?: return
    val imageRes = monumentImageRes(currentQuestion.monumentName)
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Heritage Quest", fontWeight = FontWeight.Bold) },
                actions = {
                    TextButton(onClick = onRestart) {
                        Text("Restart", color = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MediterraneanSeaBlue,
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MediterraneanWarmCream)
                .verticalScroll(scrollState)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Progress
            Column(modifier = Modifier.fillMaxWidth()) {
                LinearProgressIndicator(
                    progress = { (quizState.currentQuestionIndex + 1).toFloat() / quizState.questions.size },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = MediterraneanTerracotta,
                    trackColor = MediterraneanTerracotta.copy(alpha = 0.2f)
                )
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Question ${quizState.currentQuestionIndex + 1}/${quizState.questions.size}",
                        fontSize = 12.sp,
                        color = MediterraneanSeaBlue,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Score: ${quizState.score}",
                        fontSize = 12.sp,
                        color = MediterraneanSeaBlue,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Timer
            if (quizState.isTimerEnabled && !quizState.answered) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (quizState.timeRemaining <= 5) IncorrectAnswerRed.copy(alpha = 0.1f) else MediterraneanSunYellow.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "⏱️ Time Remaining: ${quizState.timeRemaining}s",
                        modifier = Modifier.padding(8.dp).align(Alignment.CenterHorizontally),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (quizState.timeRemaining <= 5) IncorrectAnswerRed else MediterraneanDarkSeaBlue
                    )
                }
            }

            // Monument Image
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = currentQuestion.monumentName,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Surface(
                        modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth(),
                        color = Color.Black.copy(alpha = 0.6f)
                    ) {
                        Text(
                            text = currentQuestion.monumentName,
                            modifier = Modifier.padding(8.dp),
                            color = Color.White,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Text(
                text = "Where is this monument located?",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MediterraneanDarkSeaBlue
            )

            // Options
            currentQuestion.options.forEach { option ->
                val isSelected = quizState.selectedOption == option
                val isCorrect = quizState.answered && option == currentQuestion.correctLocation
                val isWrong = quizState.answered && isSelected && !isCorrect
                
                val containerColor = when {
                    isCorrect -> CorrectAnswerGreen
                    isWrong -> IncorrectAnswerRed
                    isSelected && !quizState.answered -> MediterraneanSeaBlue.copy(alpha = 0.7f)
                    else -> MediterraneanSeaBlue
                }

                Button(
                    onClick = { if (!quizState.answered) onAnswerSelected(option) },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = containerColor,
                        disabledContainerColor = containerColor.copy(alpha = 0.6f)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !quizState.answered
                ) {
                    Text(option, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                }
            }

            // Fact Display
            if (quizState.answered) {
                val isCorrect = quizState.selectedOption == currentQuestion.correctLocation
                
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isCorrect) Color(0xFFE8F5E9) else Color(0xFFFBE9E7)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = if (isCorrect) "✨ Correct!" else "❌ Incorrect",
                            fontWeight = FontWeight.Bold,
                            color = if (isCorrect) CorrectAnswerGreen else IncorrectAnswerRed,
                            fontSize = 18.sp
                        )
                        if (!isCorrect) {
                            Text(
                                text = "It's actually in ${currentQuestion.correctLocation}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MediterraneanDarkSeaBlue
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                .padding(12.dp)
                        ) {
                            Column {
                                Text(
                                    "💡 Heritage Fact:",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = MediterraneanTerracotta
                                )
                                Text(
                                    text = currentQuestion.fact,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MediterraneanDarkSeaBlue
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = onNextQuestion,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = MediterraneanTerracotta)
                        ) {
                            Text(
                                if (quizState.currentQuestionIndex + 1 == quizState.questions.size) "Finish Quiz" else "Next Question",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun monumentImageRes(monumentName: String): Int {
    val name = monumentName.lowercase()
    return when {
        name.contains("el jem") -> R.drawable.el_jem
        name.contains("dougga") || name.contains("dogga") -> R.drawable.dogga
        name.contains("antonine") -> R.drawable.antonine_baths
        name.contains("sbeitla") -> R.drawable.sbeitla
        name.contains("bulla") -> R.drawable.bulla_regia
        name.contains("tophet") -> R.drawable.carthage_tophet
        name.contains("zaghouan") -> R.drawable.zaghouan_aqueduct
        name.contains("chemtou") -> R.drawable.chemtou
        name.contains("uthina") || name.contains("oudna") -> R.drawable.uthina
        name.contains("makthar") -> R.drawable.makthar
        else -> R.drawable.ic_roman_column
    }
}
