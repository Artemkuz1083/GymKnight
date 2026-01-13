package com.example.gymknight.data.repository

import com.example.gymknight.data.database.dao.WorkoutDAO
import com.example.gymknight.data.entity.WorkoutEntity
import com.example.gymknight.data.relation.WorkoutWithExercises
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.*
import java.util.*

interface WorkoutRepository {

    // Найти существующую тренировку на день или создать новую
    suspend fun addWorkout(workout: WorkoutEntity): WorkoutEntity

    // Реактивный поток для UI
    fun getWorkoutByDateStateFlow(start: Long, end: Long): StateFlow<WorkoutWithExercises?>
}

class WorkoutRepositoryImpl(
    private val dao: WorkoutDAO
) : WorkoutRepository {

    private val repositoryScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    // Метод findOrCreate
    override suspend fun addWorkout(workout: WorkoutEntity): WorkoutEntity {
        val startOfDay = getStartOfDay(workout.date)
        val endOfDay = getEndOfDay(workout.date)

        val existingWorkout = dao.getWorkoutByDate(startOfDay, endOfDay)
        return if (existingWorkout != null) {
            existingWorkout
        } else {
            val id = dao.insertWorkout(workout)
            workout.copy(id = id)
        }
    }

    // Возвращает StateFlow для UI
    override fun getWorkoutByDateStateFlow(start: Long, end: Long): StateFlow<WorkoutWithExercises?> {
        return dao.getWorkoutByDateFlow(start, end)
            .stateIn(
                scope = repositoryScope,
                started = SharingStarted.Lazily,
                initialValue = null
            )
    }

    // Вспомогательные функции для начала/конца дня
    private fun getStartOfDay(timestamp: Long): Long {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = timestamp
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar.timeInMillis
    }

    private fun getEndOfDay(timestamp: Long): Long {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = timestamp
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }
        return calendar.timeInMillis
    }
}
