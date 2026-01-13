package com.example.gymknight.data.repository

import com.example.gymknight.data.database.dao.SetDAO
import com.example.gymknight.data.entity.SetEntity
import javax.inject.Inject

interface SetRepository {
    suspend fun insertSet(set: SetEntity)
    suspend fun getNextOrder(exerciseId: Long): Int
}

class SetRepositoryImpl @Inject constructor(
    private val dao: SetDAO
): SetRepository{
    override suspend fun insertSet(set: SetEntity) {
        dao.insert(set)
    }

    override suspend fun getNextOrder(exerciseId: Long): Int {
        val lastOrder = dao.getLastOrder(exerciseId) ?: 0
        return lastOrder + 1
    }
}