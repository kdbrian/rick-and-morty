package com.kdbrian.rickmorty.domain.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Entity
data class CharacterEntity(
    @SerializedName("created")
    val created: String = "",
    @SerializedName("episode")
    val episode: List<String>,
    @SerializedName("gender")
    val gender: String = "",
    @PrimaryKey
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("image")
    val image: String = "",
    @SerializedName("location")
    val location: Location,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("origin")
    val origin: Origin,
    @SerializedName("species")
    val species: String = "",
    @SerializedName("status")
    val status: String = "",
    @SerializedName("type")
    val type: String = "",
    @SerializedName("url")
    val url: String = ""
) {
    companion object {
        val sampleCharacter = CharacterEntity(
            created = "2017-11-04T18:48:46.250Z",
            episode = listOf(
                "https://rickandmortyapi.com/api/episode/1",
                "https://rickandmortyapi.com/api/episode/2",
                "https://rickandmortyapi.com/api/episode/3"
            ),
            gender = "Male",
            id = 1,
            image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
            location = Location(
                name = "Citadel of Ricks",
                url = "https://rickandmortyapi.com/api/location/3"
            ),
            name = "Rick Sanchez",
            origin = Origin(
                name = "Earth (C-137)",
                url = "https://rickandmortyapi.com/api/location/1"
            ),
            species = "Human",
            status = "Alive",
            type = "",
            url = "https://rickandmortyapi.com/api/character/1"
        )
    }
}