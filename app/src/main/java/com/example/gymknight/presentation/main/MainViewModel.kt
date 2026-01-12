package com.example.gymknight.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymknight.data.relation.WorkoutWithExercises
import com.example.gymknight.data.repository.WorkoutRepository
import com.example.gymknight.domain.GetWorkoutUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class MainViewModel(
    private val getWorkoutUseCase: GetWorkoutUseCase
) : ViewModel() {

    fun getWorkout(start: Long, end: Long): StateFlow<WorkoutWithExercises?> =
        getWorkoutUseCase(start, end)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null
            )
}