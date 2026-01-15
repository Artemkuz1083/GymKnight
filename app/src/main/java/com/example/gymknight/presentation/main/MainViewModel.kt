package com.example.gymknight.presentation.main

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymknight.data.database.dao.ExerciseCatalogDAO
import com.example.gymknight.data.entity.ExerciseCatalogEntity
import com.example.gymknight.data.entity.ExerciseEntity
import com.example.gymknight.data.relation.WorkoutWithExercises
import com.example.gymknight.data.repository.ExerciseAssetRepository
import com.example.gymknight.domain.AddCategoryUseCase
import com.example.gymknight.domain.AddExerciseUseCase
import com.example.gymknight.domain.AddSetUseCase
import com.example.gymknight.domain.AddWorkoutUseCase
import com.example.gymknight.domain.DeleteExerciseUseCase
import com.example.gymknight.domain.GetExerciseByCategoryUseCase
import com.example.gymknight.domain.GetUniqueCategoriesUseCase
import com.example.gymknight.domain.GetWorkoutByDateUseCase
import com.example.gymknight.domain.GetWorkoutUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import androidx.compose.runtime.State
import androidx.compose.runtime.snapshotFlow
import com.example.gymknight.domain.AddExerciseToWorkoutUseCase
import kotlinx.coroutines.flow.flatMapLatest
import java.util.Calendar


class MainViewModel(
    private val getWorkoutUseCase: GetWorkoutUseCase,
    private val getWorkoutByDateUseCase: GetWorkoutByDateUseCase,
    private val getExerciseByCategoryUseCase: GetExerciseByCategoryUseCase,
    private val exerciseCatalogDAO: ExerciseCatalogDAO,
    private val assetProvider: ExerciseAssetRepository,
    private val addExerciseUseCase: AddExerciseUseCase,
    private val addWorkoutUseCase: AddWorkoutUseCase,
    private val addSetUseCase: AddSetUseCase,
    private val deleteExerciseUseCase: DeleteExerciseUseCase,
    private val getUniqueCategoriesUseCase: GetUniqueCategoriesUseCase,
    private val addCategoryUseCase: AddCategoryUseCase,
    private val addExerciseToWorkoutUseCase: AddExerciseToWorkoutUseCase
    ) : ViewModel() {


    private val _selectedDate = mutableStateOf(getTodayStart())
    val selectedDate: State<Long> = _selectedDate

    fun selectDate(dateMillis: Long) {
        _selectedDate.value = dateMillis
    }

    val todayWorkout: StateFlow<WorkoutWithExercises?> =
        snapshotFlow { _selectedDate.value }
            .flatMapLatest { date ->
                getWorkoutUseCase(
                    start = getDayStart(date),
                    end = getDayEnd(date)
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null
            )


    val catalogCategories: StateFlow<List<String>> = getUniqueCategoriesUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )


    init {
        viewModelScope.launch(Dispatchers.IO) {
            val count = exerciseCatalogDAO.count()
            if (count == 0) {

                val list = assetProvider.getExercisesFromJson()
                exerciseCatalogDAO.insertAll(list)

                exerciseCatalogDAO.setAutoIncrementStart()
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


    fun getWorkoutByDate(start: Long, end: Long): StateFlow<WorkoutWithExercises?> {
        return getWorkoutByDateUseCase(start, end)
    }
    fun addSet(exerciseId: Long, weight: Double, repetitions: Int) {
        viewModelScope.launch {
            addSetUseCase(exerciseId, weight, repetitions)
        }
    }

    fun deleteExercise(exercise: ExerciseEntity) {
        viewModelScope.launch {
            deleteExerciseUseCase(exercise)
        }
    }

    fun addExercise(workoutId: Long, title: String) {
        viewModelScope.launch {
            addExerciseUseCase(workoutId, title)
        }
    }

    fun getTodayStart() = getDayStart(System.currentTimeMillis())
    fun getTodayEnd() = getDayEnd(System.currentTimeMillis())

    private fun getDayStart(dateMillis: Long): Long {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = dateMillis
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar.timeInMillis
    }

    private fun getDayEnd(dateMillis: Long): Long {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = dateMillis
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }
        return calendar.timeInMillis
    }

    fun addCategoryWithExercise(categoryName: String, exerciseName: String){
        viewModelScope.launch {
            addCategoryUseCase(categoryName,exerciseName)
        }
    }

    fun onExerciseSelected(exerciseName: String){
        viewModelScope.launch(Dispatchers.IO) {
            val date = selectedDate.value

            addExerciseToWorkoutUseCase(date, exerciseName)
        }
    }

}