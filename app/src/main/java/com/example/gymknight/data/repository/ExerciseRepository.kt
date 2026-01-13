package com.example.gymknight.data.repository

import com.example.gymknight.data.database.dao.ExerciseCatalogDAO
import com.example.gymknight.data.database.dao.ExerciseDAO
import com.example.gymknight.data.entity.ExerciseCatalogEntity
import com.example.gymknight.data.entity.ExerciseEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface ExerciseRepository  {
    suspend fun deleteExercise(exercise: ExerciseEntity): Int

    fun getExerciseByCategory(category: String): Flow<List<ExerciseCatalogEntity>>
}

class ExerciseRepositoryImpl @Inject constructor(
    private val dao: ExerciseDAO,
    private val catalogDao: ExerciseCatalogDAO
): ExerciseRepository{
    override suspend fun deleteExercise(exercise: ExerciseEntity): Int {
        return dao.deleteExercise(exercise)
    }

    override fun getExerciseByCategory(category: String): Flow<List<ExerciseCatalogEntity>>{
        return catalogDao.getExerciseByCategory(category)
    }
    
}