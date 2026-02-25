package com.kdbrian.rickmorty.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.kdbrian.rickmorty.domain.model.Location
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationsDao : BaseDao<Location> {

    @Query("SELECT * FROM location")
    fun getLocations(): Flow<List<Location>>

    @Query("SELECT * FROM location WHERE name = :id LIMIT 1")
    fun getLocationById(id: String): Location?

}