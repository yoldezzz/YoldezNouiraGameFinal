package com.example.yoldeznouiraproject.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yoldeznouiraproject.R
import com.example.yoldeznouiraproject.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(2500)
        onTimeout()
    }

    val infiniteTransition = rememberInfiniteTransition(label = "splash")
    val scale = infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(MediterraneanSeaBlue, MediterraneanDarkSeaBlue, MediterraneanSeaBlue)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + scaleIn(initialScale = 0.5f),
                exit = fadeOut()
            ) {
                androidx.compose.foundation.Image(
                    painter = painterResource(id = R.drawable.ic_roman_column),
                    contentDescription = "Roman column",
                    modifier = Modifier
                        .size(120.dp)
                        .graphicsLayer(scaleX = scale.value, scaleY = scale.value)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            AnimatedVisibility(
                visible = true,
                enter = fadeIn(animationSpec = tween(delayMillis = 300)) +
                    slideInVertically(initialOffsetY = { 50 })
            ) {
                Text(
                    text = "Tunisian Heritage Quiz",
                    fontSize = 32.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            AnimatedVisibility(
                visible = true,
                enter = fadeIn(animationSpec = tween(delayMillis = 600)) +
                    slideInVertically(initialOffsetY = { 50 })
            ) {
                Text(
                    text = "Explore Roman Heritage",
                    fontSize = 18.sp,
                    color = MediterraneanSunYellow,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.alpha(0.9f)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            AnimatedVisibility(
                visible = true,
                enter = fadeIn(animationSpec = tween(delayMillis = 900))
            ) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .width(150.dp)
                        .height(4.dp)
                        .background(
                            MediterraneanTerracotta.copy(alpha = 0.2f),
                            androidx.compose.foundation.shape.RoundedCornerShape(2.dp)
                        ),
                    color = MediterraneanTerracotta
                )
            }
        }
    }
}

@Composable
fun LinearProgressIndicator(
    modifier: Modifier = Modifier,
    color: Color = Color.White
) {
    val infiniteTransition = rememberInfiniteTransition(label = "progress")
    val progress = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Restart
        ),
        label = "progress"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(4.dp)
            .background(Color.White.copy(alpha = 0.2f), androidx.compose.foundation.shape.RoundedCornerShape(2.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(progress.value)
                .background(color, androidx.compose.foundation.shape.RoundedCornerShape(2.dp))
        )
    }
}
