package com.example.gymknight.data.repository

import com.example.gymknight.data.database.dao.SetDAO
import com.example.gymknight.data.entity.SetEntity

interface SetRepository {
    suspend fun insertSet(set: SetEntity)
    suspend fun getNextOrder(exerciseId: Long): Int

    suspend fun updateSet(set: SetEntity)

    suspend fun deleteSet(set: SetEntity)
}

class SetRepositoryImpl (
    private val dao: SetDAO
): SetRepository{
    override suspend fun insertSet(set: SetEntity) {
        dao.insert(set)
    }

    override suspend fun getNextOrder(exerciseId: Long): Int {
        val lastOrder = dao.getLastOrder(exerciseId) ?: 0
        return lastOrder + 1
    }

    override suspend fun updateSet(set: SetEntity) {
        dao.update(set)
    }

    override suspend fun deleteSet(set: SetEntity) {
        dao.delete(set)
    }
}