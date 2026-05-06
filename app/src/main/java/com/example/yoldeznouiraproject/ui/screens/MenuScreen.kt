package com.example.yoldeznouiraproject.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    onStartGame: () -> Unit,
    onCategories: () -> Unit,
    onSettings: () -> Unit,
    onExit: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Main Menu", fontWeight = FontWeight.Bold) },
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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
        ) {
            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + slideInVertically() + scaleIn()
            ) {
                Text(
                    text = "🏛️ Tunisia Heritage Quest",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MediterraneanDarkSeaBlue,
                    modifier = Modifier
                        .background(
                            MediterraneanSunYellow.copy(alpha = 0.2f),
                            RoundedCornerShape(12.dp)
                        )
                        .padding(16.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Start Game button
            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + slideInVertically(initialOffsetY = { 100 })
            ) {
                Button(
                    onClick = onStartGame,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MediterraneanTerracotta),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("🎮 Start Game", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Categories button
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(animationSpec = tween(delayMillis = 100)) +
                    slideInVertically(initialOffsetY = { 100 })
            ) {
                Button(
                    onClick = onCategories,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MediterraneanSeaBlue),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("📚 Categories", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Settings button
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(animationSpec = tween(delayMillis = 150)) +
                    slideInVertically(initialOffsetY = { 100 })
            ) {
                Button(
                    onClick = onSettings,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MediterraneanOliveGreen),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("⚙️ Settings", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Exit button
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(animationSpec = tween(delayMillis = 200)) +
                    slideInVertically(initialOffsetY = { 100 })
            ) {
                OutlinedButton(
                    onClick = onExit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    border = ButtonDefaults.outlinedButtonBorder.copy(width = 2.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MediterraneanDarkSeaBlue
                    )
                ) {
                    Text("Exit", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}