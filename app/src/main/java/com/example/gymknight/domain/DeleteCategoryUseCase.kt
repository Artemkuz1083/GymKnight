package com.example.gymknight.domain

import com.example.gymknight.data.entity.ExerciseCatalogEntity
import com.example.gymknight.data.entity.ExerciseEntity
import com.example.gymknight.data.repository.ExerciseCatalogRepository
import com.example.gymknight.data.repository.ExerciseRepository

interface DeleteCategoryUseCase {
    suspend operator fun invoke(categoryName: String)
}

class DeleteCategoryUseCaseImpl (
    private val repository: ExerciseCatalogRepository
): DeleteCategoryUseCase{
    override suspend fun invoke(categoryName: String) {
        return repository.deleteCategory(categoryName)
    }
}