package com.example.gymknight.presentation.main

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymknight.data.database.dao.ExerciseCatalogDAO
import com.example.gymknight.data.entity.ExerciseCatalogEntity
import com.example.gymknight.data.relation.WorkoutWithExercises
import com.example.gymknight.data.repository.ExerciseAssetRepository
import com.example.gymknight.data.repository.ExerciseAssetRepositoryImpl
import com.example.gymknight.data.repository.WorkoutRepository
import com.example.gymknight.domain.GetExerciseByCategoryUseCase
import com.example.gymknight.domain.GetWorkoutUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class MainViewModel(
    private val getWorkoutUseCase: GetWorkoutUseCase,
    private val getExerciseByCategoryUseCase: GetExerciseByCategoryUseCase,
    private val exerciseCatalogDAO: ExerciseCatalogDAO,
    private val assetProvider: ExerciseAssetRepository


    ) : ViewModel() {

    init {
        // Загрузка данных из JSON в БД при первом запуске
        viewModelScope.launch(Dispatchers.IO) {
            val count = exerciseCatalogDAO.count()
            if (count == 0) {
                val list = assetProvider.getExercisesFromJson()
                exerciseCatalogDAO.insertAll(list)
            }
        }
    }

    fun getExerciseByCategory(category: String): Flow<List<ExerciseCatalogEntity>>{
        return getExerciseByCategoryUseCase(category)
    }

    fun getWorkout(start: Long, end: Long): StateFlow<WorkoutWithExercises?> =
        getWorkoutUseCase(start, end)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null
            )
}