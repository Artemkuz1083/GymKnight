package com.example.gymknight.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "exercise",
    foreignKeys = [
        ForeignKey(
            entity = WorkoutEntity::class,
            parentColumns = ["id"],
            childColumns = ["workoutId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("workoutId")]
)
data class ExerciseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val workoutId: Long,
    val title: String,
)
