package com.example.gymknight.domain

import com.example.gymknight.data.entity.ExerciseCatalogEntity
import com.example.gymknight.data.relation.WorkoutWithExercises
import com.example.gymknight.data.repository.ExerciseCatalogRepository
import com.example.gymknight.data.repository.ExerciseRepository
import com.example.gymknight.data.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow

interface GetExerciseByCategoryUseCase{
    operator fun invoke(category: String): Flow<List<ExerciseCatalogEntity>>
}


class GetExerciseByCategoryUseCaseImpl (
    private val repository: ExerciseCatalogRepository
): GetExerciseByCategoryUseCase{
    override fun invoke(category: String): Flow<List<ExerciseCatalogEntity>> {
        return repository.getExerciseByCategory(category)
    }
}