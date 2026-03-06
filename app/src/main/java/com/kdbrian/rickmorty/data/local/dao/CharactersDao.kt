package com.kdbrian.rickmorty.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.kdbrian.rickmorty.domain.model.CharacterEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface CharactersDao : BaseDao<CharacterEntity>{

    @Query("SELECT * FROM characterentity")
    fun getCharacters(): Flow<List<CharacterEntity>>

    @Query("SELECT * FROM CharacterEntity WHERE id = :id LIMIT 1 ")
    fun getCharacterById(id: Int): CharacterEntity?

}