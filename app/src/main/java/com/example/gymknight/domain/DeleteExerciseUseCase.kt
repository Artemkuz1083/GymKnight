package com.example.gymknight.domain

import com.example.gymknight.data.entity.ExerciseEntity
import com.example.gymknight.data.repository.ExerciseRepository


interface DeleteExerciseUseCase {
    suspend operator fun invoke(exercise: ExerciseEntity): Int
}

class DeleteExerciseUseCaseImpl (
    private val repository: ExerciseRepository
): DeleteExerciseUseCase{
    override suspend fun invoke(exercise: ExerciseEntity): Int {
        return repository.deleteExercise(exercise)
    }
}