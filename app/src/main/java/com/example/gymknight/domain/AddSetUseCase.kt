package com.example.gymknight.domain

import com.example.gymknight.data.entity.SetEntity
import com.example.gymknight.data.repository.SetRepository

interface AddSetUseCase {
    suspend operator fun invoke(exerciseId: Long, weight: Double, repetitions: Int)
}

class AddSetUseCaseImpl(
    private val repository: SetRepository
) : AddSetUseCase {
    override suspend fun invoke(exerciseId: Long, weight: Double, repetitions: Int) {
        val nextOrder = repository.getNextOrder(exerciseId)
        val set = SetEntity(
            exerciseId = exerciseId,
            order = nextOrder,
            weight = weight,
            repetitions = repetitions
        )
        repository.insertSet(set)
    }
}
