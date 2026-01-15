package com.example.gymknight.presentation.exerciseByCategory

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.example.gymknight.data.entity.ExerciseCatalogEntity
import com.example.gymknight.presentation.main.MainViewModel
import org.koin.androidx.compose.koinViewModel
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.ui.platform.LocalContext


private val DarkBg = Color(0xFF121212)
private val CardBg = Color(0xFF1E1E1E)

data class ExerciseByCategoryVoyagerScreen(val categoryName: String) : Screen {
    @Composable
    override fun Content() {
        ExerciseByCategoryScreen(categoryName)
    }
}

@Composable
fun ExerciseByCategoryScreen(categoryName: String) {
    val navigator = LocalNavigator.current

    val activity = LocalActivity.current as? ComponentActivity
        ?: throw IllegalStateException("Activity not found")

    val viewModel: MainViewModel = koinViewModel(viewModelStoreOwner = activity)

    val exercises by viewModel.getExerciseByCategory(categoryName)
        .collectAsState(initial = emptyList())

    ExerciseByCategoryScreenContent(
        categoryName = categoryName,
        exercises = exercises,
        onBackClick = { navigator?.pop()},
        onExerciseClick = { exerciseName ->
            viewModel.onExerciseSelected(exerciseName)
            navigator?.popUntilRoot()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseByCategoryScreenContent(
    categoryName: String,
    exercises: List<ExerciseCatalogEntity>,
    onBackClick: () -> Unit = {},
    onExerciseClick: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(categoryName, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBg)
            )
        },
        containerColor = DarkBg
    ) { innerPaddings ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPaddings)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }

            items(exercises) { exercise ->
                ExerciseItem(
                    exercise,
                    onClick = {onExerciseClick(exercise.name)})
            }
        }
    }
}

@Composable
fun ExerciseItem(
    exercise: ExerciseCatalogEntity,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(CardBg)
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(Color(0xFF2C2C2C), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("💪", fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(exercise.name, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(exercise.muscleGroup, color = Color.Gray, fontSize = 12.sp)
        }

        Icon(Icons.Default.MoreVert, contentDescription = null, tint = Color.White)
    }
}

@Composable
@Preview
fun ExerciseByCategoryScreenPreview(){
    ExerciseByCategoryScreenContent(
        categoryName = "Грудь",
        exercises = listOf(
            ExerciseCatalogEntity(1, "Жим лежа", "Грудь"),
            ExerciseCatalogEntity(2, "Разведение гантелей", "Грудь")
        ),
        onExerciseClick = {}
    )
}