package com.example.gymknight.domain

import com.example.gymknight.data.entity.ExerciseCatalogEntity
import com.example.gymknight.data.repository.ExerciseCatalogRepository
import com.example.gymknight.data.repository.ExerciseCatalogRepositoryImpl
import com.example.gymknight.data.repository.ExerciseRepository
import com.example.gymknight.data.repository.WorkoutRepository

interface AddCategoryUseCase {
    suspend operator fun invoke(
        categoryName: String,
        exerciseName: String
    )
}

class AddCategoryUseCaseImpl(
    private val repository: ExerciseCatalogRepository
) : AddCategoryUseCase {

    override suspend fun invoke(categoryName: String, exerciseName: String) {

        val newExercise = ExerciseCatalogEntity(
            name = exerciseName,
            muscleGroup = categoryName
        )
        repository.insertCatalogExercise(newExercise)
    }
}