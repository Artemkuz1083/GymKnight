package com.example.gymknight.data.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.gymknight.data.entity.ExerciseEntity
import com.example.gymknight.data.entity.WorkoutEntity

data class WorkoutWithExercises(
    @Embedded val workout: WorkoutEntity,

    @Relation(
        entity = ExerciseEntity::class,
        parentColumn = "id",
        entityColumn = "workoutId"
    )
    val exercises: List<ExerciseWithSets>
)