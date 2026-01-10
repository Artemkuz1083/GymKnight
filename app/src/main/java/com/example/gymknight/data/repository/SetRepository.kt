package com.example.gymknight.data.repository

import com.example.gymknight.data.database.dao.SetDAO
import javax.inject.Inject

interface SetRepository {
    
}

class SetRepositoryImpl @Inject constructor(
    private val dao: SetDAO
): SetRepository{
    
}