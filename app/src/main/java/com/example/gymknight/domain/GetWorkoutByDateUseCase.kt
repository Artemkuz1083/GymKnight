package com.example.gymknight.domain

import com.example.gymknight.data.relation.WorkoutWithExercises
import com.example.gymknight.data.repository.WorkoutRepository
import kotlinx.coroutines.flow.StateFlow

interface GetWorkoutByDateUseCase{
    operator fun invoke(start: Long, end: Long): StateFlow<WorkoutWithExercises?>
}

class GetWorkoutByDateUseCaseImpl(
    private val repository: WorkoutRepository
): GetWorkoutByDateUseCase {
    override fun invoke(start: Long, end: Long): StateFlow<WorkoutWithExercises?> {
        return repository.getWorkoutByDateStateFlow(start,end)
    }
}

