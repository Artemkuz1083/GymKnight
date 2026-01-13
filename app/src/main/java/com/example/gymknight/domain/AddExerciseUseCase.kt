package com.example.gymknight.domain

import com.example.gymknight.data.repository.ExerciseRepository


interface AddExerciseUseCase {
    suspend operator fun invoke(
        workoutId: Long,
        title: String
    )
}

class AddExerciseUseCaseImpl(
    private val exerciseRepository: ExerciseRepository
) : AddExerciseUseCase {

    override suspend fun invoke(workoutId: Long, title: String) {
        exerciseRepository.addExercise(
                workoutId = workoutId,
                title = title
        )
    }
}