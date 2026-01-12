package com.example.gymknight.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gymknight.data.entity.ExerciseCatalogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseCatalogDAO {

    @Query("SELECT COUNT(*) FROM exercise_catalog")
    suspend fun count(): Int

    @Query("SELECT * FROM exercise_catalog ORDER BY name")
    fun getAll(): Flow<List<ExerciseCatalogEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(items: List<ExerciseCatalogEntity>)
}

