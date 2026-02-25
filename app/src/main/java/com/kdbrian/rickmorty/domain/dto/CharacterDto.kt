package com.kdbrian.rickmorty.domain.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.kdbrian.rickmorty.domain.model.Character
import com.kdbrian.rickmorty.domain.model.Location
import com.kdbrian.rickmorty.domain.model.Origin
import kotlinx.serialization.Serializable

@Serializable
data class CharacterDto(
    val created: String,
    val episode: List<String>,
    val gender: String,
    val id: Int,
    val image: String,
    val location: Location,
    val name: String,
    val origin: Origin,
    val species: String,
    val status: String,
    val type: String,
    val url: String,
    val isFavourite: Boolean
)


fun CharacterDto.toCharacter() = Character(
    created = created,
    episode = episode,
    gender = gender,
    id = id,
    image = image,
    location = location,
    name = name,
    origin = origin,
    species = species,
    status = status,
    type = type,
    url = url
)

fun Character.toCharacterDto() = CharacterDto(
    created = created,
    episode = episode,
    gender = gender,
    id = id,
    image = image,
    location = location,
    name = name,
    origin = origin,
    species = species,
    status = status,
    type = type,
    url = url,
    isFavourite = false
)