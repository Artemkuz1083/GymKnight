package com.example.gymknight.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "set",
    foreignKeys = [
        ForeignKey(
            entity = ExerciseEntity::class,
            parentColumns = ["id"],
            childColumns = ["exerciseId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("exerciseId")]
)
data class SetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val exerciseId: Long,
    val order: Int,
    val weight: Double,
    val repetitions: Int
)
