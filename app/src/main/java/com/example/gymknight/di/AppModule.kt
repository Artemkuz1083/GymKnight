package com.example.gymknight.di

import androidx.room.Room
import com.example.gymknight.data.database.AppDatabase
import com.example.gymknight.data.repository.ExerciseAssetRepository
import com.example.gymknight.data.repository.ExerciseAssetRepositoryImpl
import com.example.gymknight.data.repository.ExerciseCatalogRepository
import com.example.gymknight.data.repository.ExerciseCatalogRepositoryImpl
import com.example.gymknight.data.repository.ExerciseRepository
import com.example.gymknight.data.repository.ExerciseRepositoryImpl
import com.example.gymknight.data.repository.SetRepository
import com.example.gymknight.data.repository.SetRepositoryImpl
import com.example.gymknight.data.repository.WorkoutRepository
import com.example.gymknight.data.repository.WorkoutRepositoryImpl
import com.example.gymknight.domain.AddExerciseUseCase
import com.example.gymknight.domain.AddExerciseUseCaseImpl
import com.example.gymknight.domain.AddSetUseCase
import com.example.gymknight.domain.AddSetUseCaseImpl
import com.example.gymknight.domain.AddWorkoutUseCase
import com.example.gymknight.domain.AddWorkoutUseCaseImpl
import com.example.gymknight.domain.DeleteExerciseUseCase
import com.example.gymknight.domain.DeleteExerciseUseCaseImpl
import com.example.gymknight.domain.GetExerciseByCategoryUseCase
import com.example.gymknight.domain.GetExerciseByCategoryUseCaseImpl
import com.example.gymknight.domain.GetWorkoutByDateUseCase
import com.example.gymknight.domain.GetWorkoutByDateUseCaseImpl
import com.example.gymknight.domain.GetWorkoutUseCase
import com.example.gymknight.domain.GetWorkoutUseCaseImpl
import com.example.gymknight.presentation.main.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.binds
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

    single { ExerciseCatalogRepositoryImpl(get()) } bind ExerciseCatalogRepository::class

    single { SetRepositoryImpl(get()) } bind SetRepository::class

    single { WorkoutRepositoryImpl(get()) } bind WorkoutRepository::class

    // 4. Use Cases
    single { GetWorkoutUseCaseImpl(get()) } bind GetWorkoutUseCase::class

    single { GetExerciseByCategoryUseCaseImpl(get()) } bind GetExerciseByCategoryUseCase::class

    single { GetWorkoutByDateUseCaseImpl(get()) } bind GetWorkoutByDateUseCase::class

    single { AddExerciseUseCaseImpl(get()) } bind AddExerciseUseCase::class

    single { AddWorkoutUseCaseImpl(get()) } bind AddWorkoutUseCase::class

    single { AddSetUseCaseImpl(get()) } bind AddSetUseCase::class

    single { DeleteExerciseUseCaseImpl(get()) } bind DeleteExerciseUseCase::class


}

val viewModelModule = module {
    viewModel {
        MainViewModel(
            getWorkoutUseCase = get(),
            getExerciseByCategoryUseCase = get(),
            exerciseCatalogDAO = get(),
            assetProvider = get(),
            addExerciseUseCase = get(),
            addWorkoutUseCase = get(),
            getWorkoutByDateUseCase = get(),
            addSetUseCase = get(),
            deleteExerciseUseCase = get()
        )
    }
}