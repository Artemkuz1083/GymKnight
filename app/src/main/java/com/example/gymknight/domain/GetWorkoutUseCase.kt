package com.example.gymknight.domain

import com.example.gymknight.data.relation.WorkoutWithExercises
import com.example.gymknight.data.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetWorkoutUseCase {
    operator fun invoke(start: Long, end: Long): Flow<WorkoutWithExercises>
}


class GetWorkoutUseCaseImpl @Inject constructor(
    private val repository: WorkoutRepository
): GetWorkoutUseCase{
    override fun invoke(start: Long, end: Long): Flow<WorkoutWithExercises> {
        return repository.getWorkoutByDate(start, end)
    }
}