package com.example.gymknight.presentation.addExercise

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import com.example.gymknight.presentation.exerciseByCategory.ExerciseByCategoryVoyagerScreen
import com.example.gymknight.presentation.main.MainViewModel
import org.koin.androidx.compose.koinViewModel


val DarkBackground = Color(0xFF121212)
val CardBackground = Color(0xFF1E1E1E)

@Composable
fun AddExerciseScreen(){
    val navigator = LocalNavigator.current
    val activity = LocalActivity.current as? ComponentActivity
        ?: throw IllegalStateException("Activity not found")

    val viewModel: MainViewModel = koinViewModel(viewModelStoreOwner = activity)
    val dbCategories by viewModel.catalogCategories.collectAsState()
    val uiCategories = dbCategories.map{name -> mapMuscleGroupToUi(name)}
    AddExerciseScreenContent(
        categories = uiCategories,
        onCategoryClick = { categoryName ->
            navigator?.push(ExerciseByCategoryVoyagerScreen(categoryName))
        } ,
        onAddCategory = {category , exercise ->
            viewModel.addCategoryWithExercise(category, exercise)},
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
    onAddCategory: (String,String) -> Unit,
    onCloseClick: () -> Unit
) {
    var showMenu by remember{ mutableStateOf(false) }
    var showAddCategoryDialog by remember { mutableStateOf(false)}
    var newCategoryName by remember { mutableStateOf("") }
    var newExerciseName by remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Упражнения", color = Color.White) },
                actions = {
                    Box {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                        }

                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false },
                            modifier = Modifier.background(CardBackground)
                        ) {
                            DropdownMenuItem(
                                text = { Text("Новая категория", color = Color.White) },
                                onClick = {
                                    showMenu = false
                                    showAddCategoryDialog = true
                                }
                            )
                        }
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

        if (showAddCategoryDialog) {
            AlertDialog(
                onDismissRequest = { showAddCategoryDialog = false },
                containerColor = CardBackground,
                title = { Text("Создать категорию", color = Color.White) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Чтобы создать категорию, добавьте в нее первое упражнение:", color = Color.Gray, fontSize = 14.sp)

                        OutlinedTextField(
                            value = newCategoryName,
                            onValueChange = { newCategoryName = it },
                            label = { Text("Название категории") },
                            placeholder = { Text("Напр. Растяжка") },
                            singleLine = true,
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            )
                        )

                        OutlinedTextField(
                            value = newExerciseName,
                            onValueChange = { newExerciseName = it },
                            label = { Text("Первое упражнение") },
                            placeholder = { Text("Напр. Шпагат") },
                            singleLine = true,
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            )
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        if (newCategoryName.isNotBlank() && newExerciseName.isNotBlank()) {
                            onAddCategory(newCategoryName, newExerciseName)
                            showAddCategoryDialog = false
                            newCategoryName = ""
                            newExerciseName = ""
                        }
                    }) {
                        Text("Добавить", color = Color.Green)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddCategoryDialog = false }) {
                        Text("Отмена", color = Color.Red)
                    }
                }
            )
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
        "Грудь" -> ExerciseCategory(name, com.example.gymknight.R.drawable.chest)
        "Руки" -> ExerciseCategory(name, com.example.gymknight.R.drawable.arm)
        "Спина" -> ExerciseCategory(name, com.example.gymknight.R.drawable.back)
        "Ноги" -> ExerciseCategory(name, com.example.gymknight.R.drawable.leg)
        "Плечи" -> ExerciseCategory(name, com.example.gymknight.R.drawable.shoulder)
        "Корпус" -> ExerciseCategory(name, com.example.gymknight.R.drawable.tors)
        "Фулбоди" -> ExerciseCategory(name, com.example.gymknight.R.drawable.knghite)
        "Кардио" -> ExerciseCategory(name, com.example.gymknight.R.drawable.heart)
        "Другое" -> ExerciseCategory(name, com.example.gymknight.R.drawable.knighte1)
        else -> ExerciseCategory(name, com.example.gymknight.R.drawable.knighte1)
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
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = category.icon),
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = category.name,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(20.dp)
        )
    }
}

data class ExerciseCategory(val name: String, val icon: Int, val lastTrained: Int? = null)



@Preview
@Composable
fun AddExerciseScreenPreview() {
    val previewCategories = listOf(
        ExerciseCategory("Грудь", com.example.gymknight.R.drawable.chest),
        ExerciseCategory("Спина", com.example.gymknight.R.drawable.back),
        ExerciseCategory("Ноги", com.example.gymknight.R.drawable.leg),
        ExerciseCategory("Руки", com.example.gymknight.R.drawable.arm)
    )
    MaterialTheme {
        AddExerciseScreenContent(
            categories = previewCategories,
            onCloseClick = {},
            onAddCategory = {_,_ ->},
            onCategoryClick = {}
        )
    }
}