package com.example.gymknight.data.repository

import com.example.gymknight.data.database.dao.ExerciseCatalogDAO
import com.example.gymknight.data.entity.ExerciseCatalogEntity
import kotlinx.coroutines.flow.Flow

interface ExerciseCatalogRepository{
    fun getExerciseByCategory(category: String): Flow<List<ExerciseCatalogEntity>>
}

class ExerciseCatalogRepositoryImpl(
    private val catalogDao: ExerciseCatalogDAO
): ExerciseCatalogRepository{
    override fun getExerciseByCategory(category: String): Flow<List<ExerciseCatalogEntity>> {
        return catalogDao.getExerciseByCategory(category)
    }
}