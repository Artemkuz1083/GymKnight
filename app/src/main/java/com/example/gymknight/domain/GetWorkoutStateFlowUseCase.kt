package com.example.gymknight.domain

import com.example.gymknight.data.relation.WorkoutWithExercises
import com.example.gymknight.data.repository.WorkoutRepository
import kotlinx.coroutines.flow.StateFlow


interface GetWorkoutUseCase {
    operator fun invoke(start: Long, end: Long): StateFlow<WorkoutWithExercises?>
}



class GetWorkoutUseCaseImpl (
    private val repository: WorkoutRepository
) : GetWorkoutUseCase {

    override fun invoke(start: Long, end: Long): StateFlow<WorkoutWithExercises?> {
        return repository.getWorkoutByDateStateFlow(start, end)
    }
}
