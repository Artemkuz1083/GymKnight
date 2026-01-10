package com.example.gymknight.di

import com.example.gymknight.data.entity.WorkoutEntity
import com.example.gymknight.data.repository.ExerciseRepository
import com.example.gymknight.data.repository.ExerciseRepositoryImpl
import com.example.gymknight.data.repository.SetRepository
import com.example.gymknight.data.repository.SetRepositoryImpl
import com.example.gymknight.data.repository.WorkoutRepository
import com.example.gymknight.data.repository.WorkoutRepositoryImpl
import com.example.gymknight.presentation.main.MainViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    single {
        ExerciseRepositoryImpl(get())
    }bind ExerciseRepository::class

    single {
        SetRepositoryImpl(get())
    }bind SetRepository::class

    single {
        WorkoutRepositoryImpl(get())
    }bind WorkoutRepository::class
}

val viewModelModule = module {
    viewModel {
        MainViewModel()
    }
}