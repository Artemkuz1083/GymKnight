package com.example.gymknight.presentation.main

import android.text.format.DateUtils.isToday
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
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
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import com.example.gymknight.data.relation.ExerciseWithSets
import com.example.gymknight.navigation.AddExerciseNavigationScreen
import org.koin.compose.viewmodel.koinViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


val DarkBackground = Color(0xFF121212)
val CardBackground = Color(0xFF1E1E1E)
val AccentGreen = Color(0xFF4CAF50)
@Composable
fun MainScreen() {

    val navigator = LocalNavigator.current?.parent

    val activity = LocalActivity.current as? ComponentActivity
        ?: throw IllegalStateException("Activity not found")

    val viewModel: MainViewModel =
        org.koin.androidx.compose.koinViewModel(viewModelStoreOwner = activity)
    MainScreenContent(
        viewModel,
        onGoToAddExerciseClick = {
            navigator?.push(AddExerciseNavigationScreen())
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenContent(
    viewModel: MainViewModel,
    onGoToAddExerciseClick: () -> Unit
) {

    val todayWorkout by viewModel.todayWorkout.collectAsState()

    var showDatePicker by remember { mutableStateOf(false) }

    val selectedDate by viewModel.selectedDate
    val title = remember(selectedDate) {
        if (isToday(selectedDate)) {
            "Сегодня"
        } else {
            formatDate(selectedDate)
        }
    }

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title, color = Color.White) },
                actions = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground)
            )
        },
        containerColor = DarkBackground,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onGoToAddExerciseClick,
                containerColor = AccentGreen,
                contentColor = Color.Black,
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        }
    ) { innerPaddings ->
        val exercises = todayWorkout?.exercises ?: emptyList()

        LazyColumn(
            modifier = Modifier
                .padding(innerPaddings)
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(exercises) { exerciseWithSets ->
                ExerciseCard(exerciseWithSets, viewModel)
            }
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = selectedDate)
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { viewModel.selectDate(it) }
                    showDatePicker = false
                }) { Text("ОК", color = AccentGreen) }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Отмена", color = Color.White) }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

}

@Composable
fun ExerciseCard(exercise: ExerciseWithSets, viewModel: MainViewModel) {
    var showAddSetDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(containerColor = CardBackground), // Цвет карточки
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = exercise.exercise.title, color = Color.White, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { showAddSetDialog = true }) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                }
                IconButton(onClick = { showDeleteDialog = true }) {
                    Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Gray)
                }
            }

            if (exercise.sets.isNotEmpty()) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(exercise.sets.sortedBy { it.order }) { set ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "${set.weight}кг", color = Color.White)
                            Text(text = "${set.repetitions}пвт", color = Color.Gray, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }

    if (showAddSetDialog) {
        AddSetDialog(
            onDismiss = { showAddSetDialog = false },
            onAddSet = { weight, repetitions ->
                viewModel.addSet(exercise.exercise.id, weight, repetitions)
            }
        )
    }

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

fun isToday(dateMillis: Long): Boolean {
    val today = Calendar.getInstance()
    val date = Calendar.getInstance().apply { timeInMillis = dateMillis }

    return today.get(Calendar.YEAR) == date.get(Calendar.YEAR) &&
            today.get(Calendar.DAY_OF_YEAR) == date.get(Calendar.DAY_OF_YEAR)
}

fun formatDate(dateMillis: Long): String {
    val formatter = SimpleDateFormat("dd MMMM yyyy", Locale("ru"))
    return formatter.format(Date(dateMillis))
}



