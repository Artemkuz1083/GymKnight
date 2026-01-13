package com.example.gymknight.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.gymknight.data.entity.SetEntity

@Dao
interface SetDAO {

    @Insert
    suspend fun insert(set: SetEntity)

    @Query("SELECT MAX(`order`) FROM `set` WHERE exerciseId = :exerciseId")
    suspend fun getLastOrder(exerciseId: Long): Int?
}
