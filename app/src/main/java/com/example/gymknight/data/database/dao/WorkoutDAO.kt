package com.example.gymknight.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.gymknight.data.relation.WorkoutWithExercises
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDAO {

    @Transaction
    @Query("""
        SELECT * FROM workout
        WHERE date BETWEEN :start AND :end
        LIMIT 1
    """)
    fun getWorkoutByDate(
        start: Long,
        end: Long
    ): Flow<WorkoutWithExercises>
}