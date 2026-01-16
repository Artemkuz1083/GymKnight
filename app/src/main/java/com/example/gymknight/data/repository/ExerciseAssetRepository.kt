package com.example.gymknight.data.repository

import android.content.Context
import com.example.gymknight.data.entity.ExerciseCatalogEntity
import kotlinx.serialization.json.Json

interface ExerciseAssetRepository {
    fun getExercisesFromJson(): List<ExerciseCatalogEntity>
}

class ExerciseAssetRepositoryImpl (private val context: Context) : ExerciseAssetRepository {
    override fun getExercisesFromJson(): List<ExerciseCatalogEntity> {
        val json = context.assets
            .open("exercise.json")
            .bufferedReader()
            .use { it.readText() }
        return Json.decodeFromString(json)
    }
}