package com.example.gymknight.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout")
data class WorkoutEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: Long
)
