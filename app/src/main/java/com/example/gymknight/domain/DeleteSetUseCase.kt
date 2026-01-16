package com.example.gymknight.domain

import com.example.gymknight.data.entity.ExerciseEntity
import com.example.gymknight.data.entity.SetEntity
import com.example.gymknight.data.repository.ExerciseRepository
import com.example.gymknight.data.repository.SetRepository

interface DeleteSetUseCase {
    suspend operator fun invoke(set: SetEntity)
}

class DeleteSetUseCaseImpl (
    private val repository: SetRepository
): DeleteSetUseCase{
    override suspend fun invoke(set: SetEntity) {
        return repository.deleteSet(set)
    }
}