package com.example.yoldeznouiraproject.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yoldeznouiraproject.ui.theme.*
import com.example.yoldeznouiraproject.viewmodel.SettingsUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    settingsState: SettingsUiState,
    onToggleTimer: () -> Unit,
    onDifficultyChange: (String) -> Unit,
    onStartQuiz: () -> Unit,
    onBack: () -> Unit
) {
    data class DiffUi(
        val level: String,
        val title: String,
        val subtitle: String,
        val seconds: Int,
        val icon: androidx.compose.ui.graphics.vector.ImageVector,
        val color: Color
    )

    val diffs = listOf(
        DiffUi("EASY", "Easy", "Famous landmarks", 20, Icons.Filled.Star, MediterraneanSunYellow),
        DiffUi("MEDIUM", "Medium", "Historical sites", 15, Icons.Filled.Timer, MediterraneanTerracotta),
        DiffUi("HARD", "Hard", "Archaeological details", 10, Icons.Filled.Bolt, IncorrectAnswerRed.copy(alpha = 0.8f))
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quiz Settings", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MediterraneanSeaBlue,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MediterraneanWarmCream)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Select Difficulty", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = MediterraneanDarkSeaBlue)

            diffs.forEachIndexed { index, d ->
                val isSelected = settingsState.selectedDifficulty == d.level

                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(animationSpec = tween(delayMillis = index * 100)) +
                        slideInVertically(initialOffsetY = { 50 }),
                    modifier = Modifier.animateContentSize()
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateContentSize(),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) d.color.copy(alpha = 0.15f) else Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = if (isSelected) 4.dp else 2.dp
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Surface(
                                    color = d.color.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(
                                        imageVector = d.icon,
                                        contentDescription = null,
                                        tint = d.color,
                                        modifier = Modifier
                                            .padding(10.dp)
                                            .size(24.dp)
                                    )
                                }
                                Column {
                                    Text(d.title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MediterraneanDarkSeaBlue)
                                    Text(d.subtitle, color = MediterraneanSeaBlue, fontSize = 13.sp)
                                    Text("${d.seconds}s per question", fontSize = 12.sp, color = MediterraneanSeaBlue.copy(alpha = 0.7f))
                                }
                            }

                            Button(
                                onClick = {
                                    onDifficultyChange(d.level)
                                    onStartQuiz()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = d.color),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.height(40.dp)
                            ) {
                                Text("Play", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 16.dp),
                color = MediterraneanSeaBlue.copy(alpha = 0.2f),
                thickness = 1.dp
            )

            Text("Game Options", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = MediterraneanDarkSeaBlue)

            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + slideInVertically()
            ) {
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize(),
                    colors = CardDefaults.elevatedCardColors(containerColor = MediterraneanSeaBlue.copy(alpha = 0.08f)),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(Icons.Filled.Timer, contentDescription = null, tint = MediterraneanSeaBlue, modifier = Modifier.size(24.dp))
                            Column {
                                Text("Timer", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = MediterraneanDarkSeaBlue)
                                Text(
                                    text = if (settingsState.isTimerEnabled) {
                                        "⏱️ Active during the quiz"
                                    } else {
                                        "⏸️ Disabled for relaxed play"
                                    },
                                    color = MediterraneanSeaBlue,
                                    fontSize = 14.sp
                                )
                            }
                        }
                        Switch(
                            checked = settingsState.isTimerEnabled,
                            onCheckedChange = { onToggleTimer() },
                            colors = SwitchDefaults.colors(
                                checkedTrackColor = MediterraneanTerracotta,
                                checkedThumbColor = MediterraneanTerracotta80
                            )
                        )
                    }
                }
            }
        }
    }
}