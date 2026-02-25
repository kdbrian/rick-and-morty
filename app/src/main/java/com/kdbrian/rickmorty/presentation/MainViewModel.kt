package com.kdbrian.rickmorty.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.map
import com.kdbrian.rickmorty.domain.model.Character
import com.kdbrian.rickmorty.domain.model.Episode
import com.kdbrian.rickmorty.domain.model.Location
import com.kdbrian.rickmorty.domain.repo.CharacterRepo
import com.kdbrian.rickmorty.domain.repo.EpisodeRepo
import com.kdbrian.rickmorty.domain.repo.LocationRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val characterRepo: CharacterRepo,
    private val locationRepo: LocationRepo,
    private val episodeRepo: EpisodeRepo
) : ViewModel() {


    private val _selectedCharacter = MutableStateFlow<Character?>(null)
    val selectedCharacter = _selectedCharacter.asStateFlow()

    private val _selectedLocation = MutableStateFlow<Location?>(null)
    val selectedLocation = _selectedLocation.asStateFlow()

    private val _selectedEpisode = MutableStateFlow<Episode?>(null)
    val selectedEpisode = _selectedEpisode.asStateFlow()


    fun characters(page: Int? = 1) = characterRepo.characters(page)
    fun episodes(page: Int? = 1) = episodeRepo.episodes(page)
    fun locations(page: Int? = 1) = locationRepo.locations(page)


    fun characterById(id: Int) {
        viewModelScope.launch {
            characterRepo.characterById(id).getOrNull()?.let {
                _selectedCharacter.value = it
            }
        }
    }

    fun locationById(id: Int) {
        viewModelScope.launch {
            locationRepo.locationById(id).getOrNull()?.let {
                _selectedLocation.value = it
            }
        }
    }

    fun episodeById(id: Int) {
        viewModelScope.launch {
            episodeRepo.episodeById(id).getOrNull()?.let {
                _selectedEpisode.value = it
            }
        }
    }

}