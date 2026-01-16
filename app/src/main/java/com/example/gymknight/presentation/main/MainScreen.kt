package com.example.gymknight.presentation.main

import android.text.format.DateUtils.isToday
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import com.example.gymknight.data.entity.SetEntity
import com.example.gymknight.data.relation.ExerciseWithSets
import com.example.gymknight.navigation.AddExerciseNavigationScreen
import java.text.SimpleDateFormat
import androidx.compose.ui.text.TextStyle
import java.util.Calendar
import java.util.Date
import java.util.Locale


val DarkBackground = Color(0xFF121212)
val CardBackground = Color(0xFF1E1E1E)
val AccentGreen = Color(0xFF4CAF50)
@Composable
fun MainScreen() {

    val navigator = LocalNavigator.current

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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBackground,
                    scrolledContainerColor = DarkBackground
                )
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
        colors = CardDefaults.cardColors(containerColor = CardBackground),
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
                        var showEditSetDialog by remember { mutableStateOf(false) }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(4.dp)
                                .clickable { showEditSetDialog = true }
                        ) {
                            Text(text = "${set.weight}кг", color = Color.White)
                            Text(text = "${set.repetitions}пвт", color = Color.Gray, fontSize = 12.sp)
                        }

                        if (showEditSetDialog) {
                            EditSetDialog(
                                set = set,
                                onDismiss = { showEditSetDialog = false },
                                onDelete = {
                                    viewModel.deleteSet(set)
                                    showEditSetDialog = false
                                },
                                onUpdate = { weight, reps ->
                                    viewModel.updateSet(set.copy(weight = weight, repetitions = reps))
                                    showEditSetDialog = false
                                }
                            )
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
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Удаление упражнения") },
            text = { Text("Вы уверены, что хотите удалить это упражнение? Все подходы будут удалены.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteExercise(exercise.exercise)
                    showDeleteDialog = false
                }) {
                    Text("Удалить")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Отмена")
                }
            }
        )
    }
}

@Composable
fun EditSetDialog(
    set: SetEntity,
    onDismiss: () -> Unit,
    onDelete: () -> Unit,
    onUpdate: (Double, Int) -> Unit
) {
    var weightText by remember { mutableStateOf(set.weight.toString()) }
    var repetitionsText by remember { mutableStateOf(set.repetitions.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF1E1E1E),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Редактировать подход",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Удалить сет",
                        tint = Color(0xFFCF6679)
                    )
                }
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = weightText,
                    onValueChange = { weightText = it },
                    label = { Text("Вес (кг)", color = Color.Gray) },
                    textStyle = TextStyle(color = Color.White),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AccentGreen,
                        unfocusedBorderColor = Color.Gray
                    )
                )
                OutlinedTextField(
                    value = repetitionsText,
                    onValueChange = { repetitionsText = it },
                    label = { Text("Повторения", color = Color.Gray) },
                    textStyle = TextStyle(color = Color.White),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AccentGreen,
                        unfocusedBorderColor = Color.Gray
                    )
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val w = weightText.toDoubleOrNull() ?: set.weight
                    val r = repetitionsText.toIntOrNull() ?: set.repetitions
                    onUpdate(w, r)
                }
            ) {
                Text("Сохранить", color = AccentGreen, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена", color = Color.White)
            }
        }
    )
}
@Composable
fun AddSetDialog(
    onDismiss: () -> Unit,
    onAddSet: (weight: Double, repetitions: Int) -> Unit
) {
    var weightText by remember { mutableStateOf("") }
    var repetitionsText by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF1E1E1E),
        title = {
            Text(
                text = "Добавить подход",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = weightText,
                    onValueChange = { weightText = it },
                    label = { Text("Вес (кг)", color = Color.Gray) },
                    textStyle = TextStyle(color = Color.White),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF00E676),
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = Color(0xFF00E676)
                    )
                )
                OutlinedTextField(
                    value = repetitionsText,
                    onValueChange = { repetitionsText = it },
                    label = { Text("Повторения", color = Color.Gray) },
                    textStyle = TextStyle(color = Color.White),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF00E676),
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = Color(0xFF00E676)
                    )
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val weight = weightText.toDoubleOrNull()
                val repetitions = repetitionsText.toIntOrNull()
                if (weight != null && repetitions != null) {
                    onAddSet(weight, repetitions)
                    onDismiss()
                }
            }) {
                Text("ДОБАВИТЬ", color = Color(0xFF00E676), fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("ОТМЕНА", color = Color.White)
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



