package com.example.gymknight.domain

import com.example.gymknight.data.repository.ExerciseRepository
import com.example.gymknight.data.repository.WorkoutRepository

interface AddExerciseToWorkoutUseCase {

    suspend operator fun invoke(
        dateMillis: Long,
        exerciseTitle: String
    )
}

class AddExerciseToWorkoutUseCaseImpl(
    private val addWorkoutUseCase: AddWorkoutUseCase,
    private val addExerciseUseCase: AddExerciseUseCase
): AddExerciseToWorkoutUseCase{
    override suspend fun invoke(dateMillis: Long, exerciseTitle: String) {
        val workout = addWorkoutUseCase(dateMillis)

        addExerciseUseCase(workout.id, exerciseTitle)
    }
}