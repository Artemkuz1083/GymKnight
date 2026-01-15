package com.example.gymknight.data.repository

import com.example.gymknight.data.database.dao.ExerciseCatalogDAO
import com.example.gymknight.data.entity.ExerciseCatalogEntity
import kotlinx.coroutines.flow.Flow

interface ExerciseCatalogRepository{
    fun getExerciseByCategory(category: String): Flow<List<ExerciseCatalogEntity>>
    fun getUniqueCategories(): Flow<List<String>>

    suspend fun insertCatalogExercise(exercise: ExerciseCatalogEntity)
}

class ExerciseCatalogRepositoryImpl(
    private val dao: ExerciseCatalogDAO
): ExerciseCatalogRepository{
    override fun getExerciseByCategory(category: String): Flow<List<ExerciseCatalogEntity>> {
        return dao.getExerciseByCategory(category)
    }

    override fun getUniqueCategories(): Flow<List<String>> {
        return dao.getUniqueCategories()
    }

    override suspend fun insertCatalogExercise(exercise: ExerciseCatalogEntity) {
        dao.insertExercise(exercise)
    }
}