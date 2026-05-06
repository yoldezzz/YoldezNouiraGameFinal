package com.example.yoldeznouiraproject.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Forest
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.Star
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.yoldeznouiraproject.ui.theme.*

data class Category(
    val id: String,
    val name: String,
    val subtitle: String,
    val isAvailable: Boolean,
    val icon: ImageVector,
    val colors: List<Color>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    onCategorySelected: (String) -> Unit,
    onBack: () -> Unit
) {
    val categories = listOf(
        Category(
            id = "roman",
            name = "Roman",
            subtitle = "Amphitheaters & temples",
            isAvailable = true,
            icon = Icons.Filled.AccountBalance,
            colors = listOf(MediterraneanSeaBlue, MediterraneanSeaBlue80)
        ),
        Category(
            id = "islamic",
            name = "Islamic",
            subtitle = "Mosques & medinas",
            isAvailable = false,
            icon = Icons.Filled.Mosque,
            colors = listOf(MediterraneanOliveGreen, MediterraneanSageGreen)
        ),
        Category(
            id = "punic",
            name = "Punic",
            subtitle = "Pre‑Roman traces",
            isAvailable = false,
            icon = Icons.Filled.HistoryEdu,
            colors = listOf(MediterraneanDarkTerracotta, MediterraneanTerracotta)
        ),
        Category(
            id = "modern",
            name = "Modern",
            subtitle = "Recent heritage",
            isAvailable = false,
            icon = Icons.Filled.Star,
            colors = listOf(MediterraneanSunYellow, MediterraneanTerracotta80)
        ),
        Category(
            id = "natural",
            name = "Nature",
            subtitle = "Landscapes & parks",
            isAvailable = false,
            icon = Icons.Filled.Forest,
            colors = listOf(MediterraneanSageGreen, MediterraneanOliveGreen)
        ),
        Category(
            id = "cities",
            name = "Cities",
            subtitle = "Urban stories",
            isAvailable = false,
            icon = Icons.Filled.LocationCity,
            colors = listOf(MediterraneanDarkSeaBlue, MediterraneanSeaBlue)
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Choose a category", fontWeight = FontWeight.Bold) },
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
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MediterraneanWarmCream)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 12.dp)
        ) {
            items(categories) { category ->
                CategoryGridItem(
                    category = category,
                    onClick = { if (category.isAvailable) onCategorySelected(category.id) }
                )
            }
        }
    }
}

@Composable
private fun CategoryGridItem(
    category: Category,
    onClick: () -> Unit
) {
    val gradient = Brush.linearGradient(category.colors)
    val contentColor = Color.White

    AnimatedVisibility(
        visible = true,
        enter = fadeIn() + scaleIn() + slideInVertically()
    ) {
        Card(
            onClick = onClick,
            enabled = category.isAvailable,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.05f),
            elevation = CardDefaults.cardElevation(
                defaultElevation = if (category.isAvailable) 4.dp else 2.dp
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(gradient)
                    .padding(14.dp)
            ) {
                Column(
                    modifier = Modifier.align(Alignment.TopStart),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = category.icon,
                        contentDescription = category.name,
                        tint = contentColor,
                        modifier = Modifier.size(28.dp)
                    )
                    Text(
                        text = category.name,
                        color = contentColor,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = category.subtitle,
                        color = contentColor.copy(alpha = 0.9f),
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                if (!category.isAvailable) {
                    Surface(
                        color = Color.Black.copy(alpha = 0.5f),
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(6.dp),
                        shape = MaterialTheme.shapes.small
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Filled.Lock, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Text("Coming soon", color = Color.White, style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }
            }
        }
    }
}
