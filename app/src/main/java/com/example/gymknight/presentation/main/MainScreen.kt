package com.example.gymknight.presentation.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import com.example.gymknight.data.relation.ExerciseWithSets
import com.example.gymknight.navigation.AddExerciseNavigationScreen
import com.example.gymknight.navigation.CreateExerciseNavigationScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainScreen() {

    val navigator = LocalNavigator.current?.parent
    val bsNavigator = LocalBottomSheetNavigator.current

    val viewModel: MainViewModel = koinViewModel()

    MainScreenContent(
        viewModel,
        onGoToCreateExercisesClick = {
            navigator?.push(CreateExerciseNavigationScreen())
        },
        onGoToAddExerciseClick = {
            navigator?.push(AddExerciseNavigationScreen())
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenContent(
    viewModel: MainViewModel,
    onGoToCreateExercisesClick: () -> Unit,
    onGoToAddExerciseClick: () -> Unit
) {

    val todayWorkout by viewModel.todayWorkout.collectAsState()

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Тренировка") },
                actions = {
                    IconButton(onClick = { TODO() }) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Выбрать дату"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onGoToAddExerciseClick,
                containerColor = Color(0xFF4CAF50),
                contentColor = Color.Black,
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Добавить"
                )
            }
        }
    ) { innerPaddings ->
        val exercises = todayWorkout?.exercises ?: emptyList()

        LazyColumn(
            modifier = Modifier
                .padding(innerPaddings)
                .padding(8.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(exercises) { exerciseWithSets ->
                ExerciseCard(
                    exercise = exerciseWithSets,
                    viewModel
                )
            }
        }
    }
}

@Composable
fun ExerciseCard(
    exercise: ExerciseWithSets,
    viewModel: MainViewModel
) {
    var showAddSetDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier.wrapContentSize()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Заголовок упражнения + кнопки
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = exercise.exercise.title,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.weight(1f))

                IconButton(onClick = { showAddSetDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Добавить подход",
                        tint = Color.White
                    )
                }

                IconButton(onClick = { showDeleteDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Удалить упражнение",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Список подходов
            if (exercise.sets.isNotEmpty()) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(exercise.sets.sortedBy { it.order }) { set ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(4.dp)
                        ) {
                            Text(text = "${set.weight}кг")
                            Text(text = "${set.repetitions}пвт")
                        }
                    }
                }
            }
        }
    }

    // Диалог добавления подхода
    if (showAddSetDialog) {
        AddSetDialog(
            onDismiss = { showAddSetDialog = false },
            onAddSet = { weight, repetitions ->
                viewModel.addSet(exercise.exercise.id, weight, repetitions)
            }
        )
    }

    // Диалог подтверждения удаления упражнения
    if (showDeleteDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Удаление упражнения") },
            text = { Text("Вы уверены, что хотите удалить это упражнение? Все подходы будут удалены.") },
            confirmButton = {
                androidx.compose.material3.TextButton(onClick = {
                    viewModel.deleteExercise(exercise.exercise)
                    showDeleteDialog = false
                }) {
                    Text("Удалить")
                }
            },
            dismissButton = {
                androidx.compose.material3.TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Отмена")
                }
            }
        )
    }
}

@Composable
fun AddSetDialog(
    onDismiss: () -> Unit,
    onAddSet: (weight: Double, repetitions: Int) -> Unit
) {
    var weightText by remember { mutableStateOf("") }
    var repetitionsText by remember { mutableStateOf("") }

    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Добавить подход") },
        text = {
            Column {
                androidx.compose.material3.TextField(
                    value = weightText,
                    onValueChange = { weightText = it },
                    label = { Text("Вес (кг)") },
                    singleLine = true
                )
                androidx.compose.material3.TextField(
                    value = repetitionsText,
                    onValueChange = { repetitionsText = it },
                    label = { Text("Повторения") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            androidx.compose.material3.TextButton(onClick = {
                val weight = weightText.toDoubleOrNull()
                val repetitions = repetitionsText.toIntOrNull()
                if (weight != null && repetitions != null) {
                    onAddSet(weight, repetitions)
                    onDismiss()
                }
            }) {
                Text("Добавить")
            }
        },
        dismissButton = {
            androidx.compose.material3.TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}


