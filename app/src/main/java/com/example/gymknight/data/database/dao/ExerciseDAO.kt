package com.example.gymknight.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.example.gymknight.data.entity.ExerciseEntity

@Dao
interface ExerciseDAO {
    @Insert
    suspend fun insertExercise(exercise: ExerciseEntity)

    @Delete
    suspend fun deleteExercise(exercise: ExerciseEntity): Int
}