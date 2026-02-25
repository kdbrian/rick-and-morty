package com.kdbrian.rickmorty.domain.repo

import com.kdbrian.rickmorty.domain.model.Character
import com.kdbrian.rickmorty.domain.model.Episode
import com.kdbrian.rickmorty.domain.model.Location
import kotlinx.coroutines.flow.Flow

interface FavouritesRepo {

    val favouriteCharacters: Flow<List<Character>>
    val favouriteLocations: Flow<List<Location>>
    val favouriteEpisodes: Flow<List<Episode>>

    suspend fun toggleCharacterInFavourites(character: Character)
    suspend fun toggleLocationInFavourites(location: Location)
    suspend fun toggleEpisodeInFavourites(episode: Episode)

}