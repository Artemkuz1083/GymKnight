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
import com.example.gymknight.domain.AddCategoryUseCase
import com.example.gymknight.domain.AddCategoryUseCaseImpl
import com.example.gymknight.domain.AddExerciseToWorkoutUseCase
import com.example.gymknight.domain.AddExerciseToWorkoutUseCaseImpl
import com.example.gymknight.domain.AddExerciseUseCase
import com.example.gymknight.domain.AddExerciseUseCaseImpl
import com.example.gymknight.domain.AddSetUseCase
import com.example.gymknight.domain.AddSetUseCaseImpl
import com.example.gymknight.domain.AddWorkoutUseCase
import com.example.gymknight.domain.AddWorkoutUseCaseImpl
import com.example.gymknight.domain.DeleteCategoryUseCase
import com.example.gymknight.domain.DeleteCategoryUseCaseImpl
import com.example.gymknight.domain.DeleteExerciseUseCase
import com.example.gymknight.domain.DeleteExerciseUseCaseImpl
import com.example.gymknight.domain.DeleteSetUseCase
import com.example.gymknight.domain.DeleteSetUseCaseImpl
import com.example.gymknight.domain.GetExerciseByCategoryUseCase
import com.example.gymknight.domain.GetExerciseByCategoryUseCaseImpl
import com.example.gymknight.domain.GetUniqueCategoriesUseCase
import com.example.gymknight.domain.GetUniqueCategoriesUseCaseImpl
import com.example.gymknight.domain.GetWorkoutByDateUseCase
import com.example.gymknight.domain.GetWorkoutByDateUseCaseImpl
import com.example.gymknight.domain.GetWorkoutUseCase
import com.example.gymknight.domain.GetWorkoutUseCaseImpl
import com.example.gymknight.domain.UpdateSetUseCase
import com.example.gymknight.domain.UpdateSetUseCaseImpl
import com.example.gymknight.presentation.main.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module

val appModule = module {

    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "gym_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    single { get<AppDatabase>().exerciseDAO }
    single { get<AppDatabase>().setDAO }
    single { get<AppDatabase>().exerciseCatalogDAO }
    single { get<AppDatabase>().workoutDAO }

    // Репозитории
    single { ExerciseRepositoryImpl(get()) } bind ExerciseRepository::class

    single<ExerciseAssetRepository> { ExerciseAssetRepositoryImpl(androidContext()) }

    single { ExerciseCatalogRepositoryImpl(get()) } bind ExerciseCatalogRepository::class

    single { SetRepositoryImpl(get()) } bind SetRepository::class

    single { WorkoutRepositoryImpl(get()) } bind WorkoutRepository::class

    // Use Cases
    factory { GetWorkoutUseCaseImpl(get()) } bind GetWorkoutUseCase::class

    factory { GetExerciseByCategoryUseCaseImpl(get()) } bind GetExerciseByCategoryUseCase::class

    factory { GetWorkoutByDateUseCaseImpl(get()) } bind GetWorkoutByDateUseCase::class

    factory { AddExerciseUseCaseImpl(get()) } bind AddExerciseUseCase::class

    factory { AddWorkoutUseCaseImpl(get()) } bind AddWorkoutUseCase::class

    factory { AddSetUseCaseImpl(get()) } bind AddSetUseCase::class

    factory { DeleteExerciseUseCaseImpl(get()) } bind DeleteExerciseUseCase::class

    factory { GetUniqueCategoriesUseCaseImpl(get()) } bind GetUniqueCategoriesUseCase::class

    factory { AddCategoryUseCaseImpl(get()) } bind AddCategoryUseCase::class

    factory<AddExerciseToWorkoutUseCase> { AddExerciseToWorkoutUseCaseImpl(get(), get()) }

    factory { DeleteSetUseCaseImpl(get()) } bind DeleteSetUseCase::class

    factory { UpdateSetUseCaseImpl(get()) } bind UpdateSetUseCase::class

    factory<DeleteCategoryUseCase> { DeleteCategoryUseCaseImpl(get()) }

}

val viewModelModule = module {
    viewModelOf(::MainViewModel)
}