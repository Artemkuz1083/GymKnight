package com.example.gymknight.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymknight.data.database.dao.ExerciseCatalogDAO
import com.example.gymknight.data.entity.ExerciseCatalogEntity
import com.example.gymknight.data.entity.ExerciseEntity
import com.example.gymknight.data.relation.WorkoutWithExercises
import com.example.gymknight.data.repository.ExerciseAssetRepository
import com.example.gymknight.domain.AddExerciseUseCase
import com.example.gymknight.domain.AddSetUseCase
import com.example.gymknight.domain.AddWorkoutUseCase
import com.example.gymknight.domain.GetExerciseByCategoryUseCase
import com.example.gymknight.domain.GetWorkoutByDateUseCase
import com.example.gymknight.domain.GetWorkoutUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class MainViewModel(
    private val getWorkoutUseCase: GetWorkoutUseCase,
    private val getWorkoutByDateUseCase: GetWorkoutByDateUseCase,
    private val getExerciseByCategoryUseCase: GetExerciseByCategoryUseCase,
    private val exerciseCatalogDAO: ExerciseCatalogDAO,
    private val assetProvider: ExerciseAssetRepository,
    private val addExerciseUseCase: AddExerciseUseCase,
    private val addWorkoutUseCase: AddWorkoutUseCase,
    private val addSetUseCase: AddSetUseCase
    ) : ViewModel() {

    val todayWorkout: StateFlow<WorkoutWithExercises?> =
        getWorkoutUseCase(
            start = getTodayStart(),
            end = getTodayEnd()
        )


    init {
        // Загрузка данных из JSON в БД при первом запуске
        viewModelScope.launch(Dispatchers.IO) {
            val count = exerciseCatalogDAO.count()
            if (count == 0) {
                val list = assetProvider.getExercisesFromJson()
                exerciseCatalogDAO.insertAll(list)
            }
        }

        // Добавляем тренировку на сегодня и потом упражнения, когда она появится
        viewModelScope.launch {
            val workout = addWorkoutUseCase(getTodayStart()) // findOrCreate
//            addExerciseUseCase(workout.id, "Жим лежа")
//            addExerciseUseCase(workout.id, "Тяга верхнего блока")
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


    fun getWorkoutByDate(start: Long, end: Long): StateFlow<WorkoutWithExercises?> {
        return getWorkoutByDateUseCase(start, end)
    fun addSet(exerciseId: Long, weight: Double, repetitions: Int) {
        viewModelScope.launch {
            addSetUseCase(exerciseId, weight, repetitions)
        }
    }

    fun deleteExercise(exercise: ExerciseEntity) {
        
    }

    fun addExercise(workoutId: Long, title: String) {
        viewModelScope.launch {
            addExerciseUseCase(workoutId, title)
        }
    }


    fun addTodayWorkout() {
        viewModelScope.launch {
            addWorkoutUseCase(getTodayStart())
        }
    }

    fun getTodayStart(): Long {
        val calendar = java.util.Calendar.getInstance().apply {
            set(java.util.Calendar.HOUR_OF_DAY, 0)
            set(java.util.Calendar.MINUTE, 0)
            set(java.util.Calendar.SECOND, 0)
            set(java.util.Calendar.MILLISECOND, 0)
        }
        return calendar.timeInMillis
    }

    fun getTodayEnd(): Long {
        val calendar = java.util.Calendar.getInstance().apply {
            set(java.util.Calendar.HOUR_OF_DAY, 23)
            set(java.util.Calendar.MINUTE, 59)
            set(java.util.Calendar.SECOND, 59)
            set(java.util.Calendar.MILLISECOND, 999)
        }
        return calendar.timeInMillis
    }

}