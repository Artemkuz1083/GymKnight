package com.example.gymknight.data.database.dao

import androidx.room.Dao
import androidx.room.Upsert
import com.example.gymknight.data.entity.ExerciseEntity

@Dao
interface ExerciseDAO {
    @Upsert
    suspend fun upsertExercise(exerciseEntity: ExerciseEntity)
}