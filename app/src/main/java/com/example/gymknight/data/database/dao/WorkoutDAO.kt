package com.example.gymknight.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.gymknight.data.entity.WorkoutEntity
import com.example.gymknight.data.relation.WorkoutWithExercises
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDAO {

    @Insert
    suspend fun insertWorkout(workout: WorkoutEntity): Long

    @Transaction
    @Query("""
        SELECT * FROM workout
        WHERE date BETWEEN :start AND :end
        LIMIT 1
    """)
    fun getWorkoutByDateFlow(start: Long, end: Long): Flow<WorkoutWithExercises>

    @Query("SELECT * FROM workout WHERE date BETWEEN :start AND :end LIMIT 1")
    suspend fun getWorkoutByDate(start: Long, end: Long): WorkoutEntity?
}
