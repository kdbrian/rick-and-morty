package com.kdbrian.rickmorty.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.kdbrian.rickmorty.domain.model.AppCounter
import kotlinx.coroutines.flow.Flow

@Dao
interface AppCounterDao : BaseDao<AppCounter> {

    @Query("SELECT * FROM appcounter ORDER BY timestamp DESC")
    fun counter() : Flow<List<AppCounter>>

}