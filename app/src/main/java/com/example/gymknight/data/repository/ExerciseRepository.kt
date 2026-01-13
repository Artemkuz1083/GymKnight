package com.example.gymknight.data.repository

import com.example.gymknight.data.database.dao.ExerciseCatalogDAO
import com.example.gymknight.data.database.dao.ExerciseDAO
import com.example.gymknight.data.entity.ExerciseCatalogEntity
import com.example.gymknight.data.entity.ExerciseEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface ExerciseRepository  {
    suspend fun deleteExercise(exercise: ExerciseEntity): Int

    suspend fun addExercise( workoutId: Long, title: String)
}

class ExerciseRepositoryImpl @Inject constructor(
    private val dao: ExerciseDAO,
): ExerciseRepository{
    override suspend fun deleteExercise(exercise: ExerciseEntity): Int {
        return dao.deleteExercise(exercise)
    }


    override suspend fun addExercise(workoutId: Long, title: String) {
        dao.insertExercise(
            ExerciseEntity(
                workoutId = workoutId,
                title = title
            )
        )
    }
}