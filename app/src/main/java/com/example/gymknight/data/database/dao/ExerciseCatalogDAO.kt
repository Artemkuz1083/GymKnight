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

    @Query("SELECT * FROM exercise_catalog WHERE muscleGroup = :category ORDER BY name ")
    fun getExerciseByCategory(category: String): Flow<List<ExerciseCatalogEntity>>

    @Query("SELECT DISTINCT muscleGroup FROM exercise_catalog")
    fun getUniqueCategories(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercise(exercise: ExerciseCatalogEntity)

    @Query("UPDATE sqlite_sequence SET seq = 10000 WHERE name = 'exercise_catalog'")
    suspend fun setAutoIncrementStart()

    @Query("INSERT OR IGNORE INTO sqlite_sequence (name, seq) VALUES ('exercise_catalog', 10000)")
    suspend fun ensureSequenceTableExists()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(items: List<ExerciseCatalogEntity>)
}

