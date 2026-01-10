package com.example.gymknight.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.gymknight.data.database.dao.ExerciseDAO
import com.example.gymknight.data.database.dao.SetDAO
import com.example.gymknight.data.database.dao.WorkoutDAO
import com.example.gymknight.data.entity.ExerciseEntity
import com.example.gymknight.data.entity.SetEntity
import com.example.gymknight.data.entity.WorkoutEntity


@Database(
    entities = [
        ExerciseEntity::class,
        SetEntity::class,
        WorkoutEntity::class
    ],
    version = 1
)

abstract class AppDatabase : RoomDatabase(){
    abstract val exerciseDAO: ExerciseDAO
    abstract val setDAO: SetDAO
    abstract val workoutDAO: WorkoutDAO
}