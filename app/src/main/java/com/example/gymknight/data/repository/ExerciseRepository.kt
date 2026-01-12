package com.example.gymknight.data.repository

import com.example.gymknight.data.database.dao.ExerciseDAO
import com.example.gymknight.data.entity.ExerciseEntity
import javax.inject.Inject

interface ExerciseRepository  {
    suspend fun deleteExercise(exercise: ExerciseEntity): Int


}

class ExerciseRepositoryImpl @Inject constructor(
    private val dao: ExerciseDAO
): ExerciseRepository{
    override suspend fun deleteExercise(exercise: ExerciseEntity): Int {
        return dao.deleteExercise(exercise)
    }
    
}