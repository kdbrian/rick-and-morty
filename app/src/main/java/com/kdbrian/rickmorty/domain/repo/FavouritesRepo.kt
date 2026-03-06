package com.kdbrian.rickmorty.domain.repo

import com.kdbrian.rickmorty.domain.model.CharacterEntity
import com.kdbrian.rickmorty.domain.model.Episode
import com.kdbrian.rickmorty.domain.model.Location
import kotlinx.coroutines.flow.Flow

interface FavouritesRepo {

    val favouriteCharacters: Flow<List<CharacterEntity>>
    val favouriteLocations: Flow<List<Location>>
    val favouriteEpisodes: Flow<List<Episode>>

    suspend fun toggleCharacterInFavourites(characterEntity: CharacterEntity)
    suspend fun toggleLocationInFavourites(location: Location)
    suspend fun toggleEpisodeInFavourites(episode: Episode)

}