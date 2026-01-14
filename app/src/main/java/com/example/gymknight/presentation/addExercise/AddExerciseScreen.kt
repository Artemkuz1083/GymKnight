package com.example.gymknight.presentation.addExercise

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import com.example.gymknight.presentation.exerciseByCategory.ExerciseByCategoryScreen
import com.example.gymknight.presentation.exerciseByCategory.ExerciseByCategoryScreenContent
import com.example.gymknight.presentation.exerciseByCategory.ExerciseByCategoryVoyagerScreen
import com.example.gymknight.presentation.exercises.ExercisesScreen
import com.example.gymknight.presentation.main.MainViewModel
import org.koin.androidx.compose.koinViewModel


val DarkBackground = Color(0xFF121212)
val CardBackground = Color(0xFF1E1E1E)
val PurpleCard = Color(0xFF2D1B33)
val GreenCard = Color(0xFF1B2D1B)
val BlueCard = Color(0xFF1B1B2D)

@Composable
fun AddExerciseScreen(){
    val navigator = LocalNavigator.current
    val viewModel: MainViewModel = koinViewModel ()
    val dbCategories by viewModel.catalogCategories.collectAsState()
    val uiCategories = dbCategories.map{name -> mapMuscleGroupToUi(name)}
    AddExerciseScreenContent(
        categories = uiCategories,
        onCategoryClick = { categoryName ->
            navigator?.push(ExerciseByCategoryVoyagerScreen(categoryName))
        } ,
        onCloseClick = {
            navigator?.pop()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExerciseScreenContent(
    categories: List<ExerciseCategory>,
    onCategoryClick: (String) -> Unit,
    onCloseClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Упражнения", color = Color.White) },
                actions = {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                    }
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Default.Search, contentDescription = null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground)
            )
        },
        containerColor = DarkBackground
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                QuickActionsGrid()
                Spacer(modifier = Modifier.height(16.dp))
            }

            items(categories) { category ->
                CategoryItem(
                    category = category,
                    onClick = {
                        onCategoryClick(category.name)
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
            Text(
                text = "ЗАКРЫТЬ",
                modifier = Modifier
                    .padding(bottom = 64.dp, end = 20.dp)
                    .background(Color(0xFF252525), RoundedCornerShape(8.dp))
                    .clickable {
                        onCloseClick()
                    }
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

fun mapMuscleGroupToUi(name: String): ExerciseCategory {
    return when (name) {
        "Грудь" -> ExerciseCategory(name, Icons.Default.FitnessCenter, Color(0xFF81C784))
        "Руки" -> ExerciseCategory(name, Icons.Default.PanTool, Color(0xFFE57373))
        "Спина" -> ExerciseCategory(name, Icons.Default.Cloud, Color(0xFF64B5F6))
        "Ноги" -> ExerciseCategory(name, Icons.Default.Accessibility, Color(0xFFDCE775))
        "Плечи" -> ExerciseCategory(name, Icons.Default.Person, Color(0xFF4FC3F7))
        "Корпус" -> ExerciseCategory(name, Icons.Default.Casino, Color(0xFFBA68C8))
        "Другое" -> ExerciseCategory(name, Icons.Default.Build, Color(0xFF90A4AE))
        else -> ExerciseCategory(name, Icons.Default.MoreHoriz, Color(0xFF78909C))
    }
}

@Composable
fun QuickActionsGrid() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.height(100.dp)) {
            ActionCard("Из программы", Icons.Default.FitnessCenter, PurpleCard, Color(0xFFE195FF), Modifier.weight(1f))
            ActionCard("Из другого дня", Icons.Default.DateRange, GreenCard, Color(0xFF81C784), Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.height(100.dp)) {
            ActionCard("Недавние упражнения", Icons.Default.History, BlueCard, Color(0xFF64B5F6), Modifier.weight(1f))
            ActionCard("Добавить комментарий", Icons.Default.Menu, CardBackground, Color(0xFF90A4AE), Modifier.weight(1f))
        }
    }
}

@Composable
fun ActionCard(title: String, icon: ImageVector, bgColor: Color, tint: Color, modifier: Modifier) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, contentDescription = null, tint = tint)
            Spacer(modifier = Modifier.height(8.dp))
            Text(title, color = tint, fontSize = 14.sp, fontWeight = FontWeight.Medium, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        }
    }
}

@Composable
fun CategoryItem(category: ExerciseCategory, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(CardBackground)
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(category.icon, contentDescription = null, tint = category.color, modifier = Modifier.size(28.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(category.name, color = Color.White, fontSize = 18.sp, modifier = Modifier.weight(1f))
    }
}

data class ExerciseCategory(val name: String, val icon: ImageVector, val color: Color, val lastTrained: Int? = null)



@Preview
@Composable
fun AddExerciseScreenPreview() {
    MaterialTheme {
        AddExerciseScreenContent(
            categories = emptyList(),
            onCloseClick = {},
            onCategoryClick = {}
        )
    }
}