package com.example.gymknight.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.example.gymknight.presentation.RootScreen
import com.example.gymknight.presentation.addSet.AddSetScreen
import com.example.gymknight.presentation.addExercise.AddExerciseScreen
import com.example.gymknight.presentation.exercises.ExercisesScreen
import com.example.gymknight.presentation.main.MainScreen

class MainNavigationScreen : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(
                Icons.Default.Home
            )
            return remember {
                TabOptions(
                    index = 0u,
                    title = "Main",
                    icon = icon
                )
            }
        }
    @Composable
    override fun Content() {
        MainScreen()
    }

}

class ExercisesNavigationScreen : Tab{
    override val options: TabOptions
        @Composable
        get() {

            val icon = rememberVectorPainter(
                Icons.Default.Settings
            )
            return remember {
                TabOptions(
                    index = 1u,
                    title = "Settings",
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        ExercisesScreen()
    }

}

class RootNavigationScreen: Screen{
    @Composable
    override fun Content() {
        RootScreen()
    }

}

class AddExerciseNavigationScreen: Screen{
    @Composable
    override fun Content(){
        AddExerciseScreen()
    }
}

class CreateExerciseNavigationScreen: Screen{
    @Composable
    override fun Content() {
        AddSetScreen()
    }
}