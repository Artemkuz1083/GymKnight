package com.example.gymknight.domain

import com.example.gymknight.data.entity.WorkoutEntity
import com.example.gymknight.data.repository.WorkoutRepository

interface AddWorkoutUseCase {
    suspend operator fun invoke(date: Long): WorkoutEntity
}

class AddWorkoutUseCaseImpl(
    private val workoutRepository: WorkoutRepository
) : AddWorkoutUseCase {
    override suspend fun invoke(date: Long): WorkoutEntity {
        return workoutRepository.addWorkout(WorkoutEntity(date = date))
    }
}