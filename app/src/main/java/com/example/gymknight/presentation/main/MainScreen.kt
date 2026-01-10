package com.example.gymknight.presentation.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import com.example.gymknight.data.entity.ExerciseEntity

@Composable
fun MainScreen() {
    MainScreenContent(
        TODO()
    )

}


@Composable
fun MainScreenContent(
    exerciseEntity: ExerciseEntity
){
    val context = LocalContext.current

    Scaffold { innerPaddings ->
        Box(
            modifier = Modifier
                .padding(innerPaddings)
                .fillMaxSize()
        ){
            LazyColumn {
                items(
                    TODO()
                ){
                    TODO()
                }
            }
        }
    }
}

fun ExerciseItem(){

}

@Composable
@Preview
fun MainScreenPreview() {
   MainScreenContent(
        TODO()
    )
}