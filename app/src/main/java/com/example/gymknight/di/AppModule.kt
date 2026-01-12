package com.example.gymknight.di

import com.example.gymknight.data.database.dao.ExerciseDAO
import com.example.gymknight.data.database.dao.SetDAO
import com.example.gymknight.data.database.dao.WorkoutDAO
import com.example.gymknight.data.entity.ExerciseEntity
import com.example.gymknight.data.entity.WorkoutEntity
import com.example.gymknight.data.relation.WorkoutWithExercises
import com.example.gymknight.data.repository.ExerciseRepository
import com.example.gymknight.data.repository.ExerciseRepositoryImpl
import com.example.gymknight.data.repository.SetRepository
import com.example.gymknight.data.repository.SetRepositoryImpl
import com.example.gymknight.data.repository.WorkoutRepository
import com.example.gymknight.data.repository.WorkoutRepositoryImpl
import com.example.gymknight.domain.GetWorkoutUseCase
import com.example.gymknight.domain.GetWorkoutUseCaseImpl
import com.example.gymknight.presentation.main.MainViewModel
import kotlinx.coroutines.flow.Flow
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {

    single {
        object : ExerciseDAO{
            override suspend fun upsertExercise(exerciseEntity: ExerciseEntity) {
                TODO()
            }

            override suspend fun deleteExercise(exercise: ExerciseEntity): Int {
                TODO()
            }
        }
    }bind ExerciseDAO::class

    single {
        object : SetDAO{

        }
    }bind SetDAO::class

    single {
        object : WorkoutDAO{
            override fun getWorkoutByDate(
                start: Long,
                end: Long
            ): Flow<WorkoutWithExercises> {
                TODO("Not yet implemented")
            }
        }
    }bind WorkoutDAO::class

    single {
        ExerciseRepositoryImpl(get())
    }bind ExerciseRepository::class

    single {
        SetRepositoryImpl(get())
    }bind SetRepository::class

    single {
        WorkoutRepositoryImpl(get())
    }bind WorkoutRepository::class

    single {
        GetWorkoutUseCaseImpl(get())
    }bind GetWorkoutUseCase::class
}

val viewModelModule = module {
    viewModel {
        MainViewModel(get())
    }
}