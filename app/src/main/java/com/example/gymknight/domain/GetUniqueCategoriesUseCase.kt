package com.example.gymknight.domain

import com.example.gymknight.data.entity.ExerciseCatalogEntity
import com.example.gymknight.data.repository.ExerciseCatalogRepository
import kotlinx.coroutines.flow.Flow

interface GetUniqueCategoriesUseCase{
    operator fun invoke(): Flow<List<String>>
}


class GetUniqueCategoriesUseCaseImpl (
    private val repository: ExerciseCatalogRepository
): GetUniqueCategoriesUseCase{
    override fun invoke(): Flow<List<String>> {
        return repository.getUniqueCategories()
    }
}