package com.example.gymknight.di

import androidx.room.Room
import com.example.gymknight.data.database.AppDatabase
import com.example.gymknight.data.database.dao.ExerciseCatalogDAO
import com.example.gymknight.data.database.dao.ExerciseDAO
import com.example.gymknight.data.database.dao.SetDAO
import com.example.gymknight.data.database.dao.WorkoutDAO
import com.example.gymknight.data.repository.ExerciseAssetRepository
import com.example.gymknight.data.repository.ExerciseAssetRepositoryImpl
import com.example.gymknight.data.repository.ExerciseRepository
import com.example.gymknight.data.repository.ExerciseRepositoryImpl
import com.example.gymknight.data.repository.SetRepository
import com.example.gymknight.data.repository.SetRepositoryImpl
import com.example.gymknight.data.repository.WorkoutRepository
import com.example.gymknight.data.repository.WorkoutRepositoryImpl
import com.example.gymknight.domain.GetWorkoutUseCase
import com.example.gymknight.domain.GetWorkoutUseCaseImpl
import com.example.gymknight.presentation.main.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {

    // 1. Создаем синглтон базы данных Room
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "gym_database"
        )
            // Позволяет Room пересоздавать таблицы, если ты изменил Entity,
            // но не хочешь возиться с миграциями на этапе разработки
            .fallbackToDestructiveMigration()
            .build()
    }

    // 2. Предоставляем DAO напрямую из экземпляра базы данных
    single { get<AppDatabase>().exerciseDAO }
    single { get<AppDatabase>().setDAO }
    single { get<AppDatabase>().exerciseCatalogDAO }
    single { get<AppDatabase>().workoutDAO }

    // 3. Репозитории (привязываем интерфейсы к реализациям)
    single { ExerciseRepositoryImpl(get()) } bind ExerciseRepository::class

    single<ExerciseAssetRepository> { ExerciseAssetRepositoryImpl(androidContext()) }

    single { SetRepositoryImpl(get()) } bind SetRepository::class

    single { WorkoutRepositoryImpl(get()) } bind WorkoutRepository::class

    // 4. Use Cases
    single { GetWorkoutUseCaseImpl(get()) } bind GetWorkoutUseCase::class
}

val viewModelModule = module {
    viewModel {
        MainViewModel(
            getWorkoutUseCase = get(),
            exerciseCatalogDAO = get(),
            assetProvider = get()
        )
    }
}