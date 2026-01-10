package com.example.gymknight.data.repository

import com.example.gymknight.data.database.dao.WorkoutDAO
import com.example.gymknight.data.relation.WorkoutWithExercises
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface WorkoutRepository {
    fun getWorkoutByDate(
        start: Long,
        end: Long
    ): Flow<WorkoutWithExercises>
}

class WorkoutRepositoryImpl @Inject constructor(
    private val dao: WorkoutDAO
): WorkoutRepository{
    override fun getWorkoutByDate(start: Long, end: Long): Flow<WorkoutWithExercises> {
        return dao.getWorkoutByDate(start, end)
    }
}