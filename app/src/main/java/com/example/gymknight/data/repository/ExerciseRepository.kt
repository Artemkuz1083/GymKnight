package com.example.gymknight.data.repository

import com.example.gymknight.data.database.dao.ExerciseDAO
import javax.inject.Inject

interface ExerciseRepository  {

}

class ExerciseRepositoryImpl @Inject constructor(
    private val dao: ExerciseDAO
): ExerciseRepository{
    
}