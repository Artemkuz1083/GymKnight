package com.example.gymknight.data.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.gymknight.data.entity.ExerciseEntity
import com.example.gymknight.data.entity.SetEntity

data class ExerciseWithSets(
    @Embedded val exercise: ExerciseEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "exerciseId"
    )
    val sets: List<SetEntity>
)