package com.example.gymknight.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.example.gymknight.presentation.addExercise.AddExerciseScreen
import com.example.gymknight.presentation.main.MainScreen

class MainNavigationScreen : Screen {
    @Composable
    override fun Content() {
        MainScreen()
    }

}

class AddExerciseNavigationScreen: Screen{
    @Composable
    override fun Content(){
        AddExerciseScreen()
    }
}