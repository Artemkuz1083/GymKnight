package com.example.gymknight.presentation.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import com.example.gymknight.navigation.AddExerciseNavigationScreen
import com.example.gymknight.navigation.CreateExerciseNavigationScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainScreen() {

    val navigator = LocalNavigator.current?.parent
    val bsNavigator = LocalBottomSheetNavigator.current

    val viewModel: MainViewModel = koinViewModel()

    MainScreenContent(
        onGoToCreateExercisesClick = {
            navigator?.push(CreateExerciseNavigationScreen())
        },
        onGoToAddExerciseClick = {
            navigator?.push(AddExerciseNavigationScreen())
        }
    )

}


@Composable
fun MainScreenContent(
    onGoToCreateExercisesClick: () -> Unit,
    onGoToAddExerciseClick: () -> Unit
) {
    val context = LocalContext.current

    val langs = listOf("Kotlin", "Java", "JavaScript", "Python", "JavaScript", "Python", "JavaScript", "Python", "JavaScript", "Python", "JavaScript", "Python")


    Scaffold { innerPaddings ->
        Box(
            modifier = Modifier
                .padding(innerPaddings)
                .padding(16.dp)
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            LazyColumn(
                modifier = Modifier
            ) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.tertiary,
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 6.dp
                        ),
                        modifier = Modifier
                            .wrapContentSize()
                    ) {
                        Row {
                            Text(
                                text = "Жим штанги лежа",
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .padding(16.dp),
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            IconButton (onGoToCreateExercisesClick){
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                            IconButton(onClick = {}) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }

                        }

                        LazyRow(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalArrangement  = Arrangement.spacedBy(12.dp)
                        ) {
                            items(langs) { lang -> Column {
                                Text("123")
                                Text(lang)
                                }
                            }
                        }
                    }
                }
            }
            FloatingActionButton(
                onClick = onGoToAddExerciseClick,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 96.dp, end = 24.dp),

                containerColor = Color(0xFF4CAF50),
                contentColor = Color.Black,
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Добавить"
                )
            }
        }
    }
}


@Composable
@Preview
fun MainScreenPreview() {
    MainScreenContent(
        onGoToCreateExercisesClick ={},
        onGoToAddExerciseClick = {}
    )
}