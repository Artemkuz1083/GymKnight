package com.example.gymknight.domain

import com.example.gymknight.data.entity.SetEntity
import com.example.gymknight.data.repository.SetRepository

interface UpdateSetUseCase {
    suspend operator fun invoke(set: SetEntity)
}

class UpdateSetUseCaseImpl (
    private val repository: SetRepository
): UpdateSetUseCase{
    override suspend fun invoke(set: SetEntity) {
        return repository.updateSet(set)
    }
}