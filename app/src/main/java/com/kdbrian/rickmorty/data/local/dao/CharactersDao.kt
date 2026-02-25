package com.kdbrian.rickmorty.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.kdbrian.rickmorty.domain.model.Character
import kotlinx.coroutines.flow.Flow


@Dao
interface CharactersDao : BaseDao<Character>{

    @Query("SELECT * FROM character")
    fun getCharacters(): Flow<List<Character>>

    @Query("SELECT * FROM Character WHERE id = :id LIMIT 1 ")
    fun getCharacterById(id: Int): Character?

}