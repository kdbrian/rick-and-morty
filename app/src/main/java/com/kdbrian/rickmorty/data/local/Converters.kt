package com.kdbrian.rickmorty.data.local

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.kdbrian.rickmorty.domain.model.Location
import com.kdbrian.rickmorty.domain.model.Origin
import kotlin.jvm.java

object Converters {

    private val json = Gson().newBuilder().create()

    @TypeConverter
    fun fromOrigin(origin: Origin): String {
        return json.toJson(origin)
    }

    @TypeConverter
    fun toOrigin(origin: String): Origin {
        return json.fromJson(origin, Origin::class.java)
    }

    @TypeConverter
    fun fromLocation(location: Location): String {
        return json.toJson(location)
    }

    @TypeConverter
    fun toLocation(location: String): Location {
        return json.fromJson(location, Location::class.java)
    }

    @TypeConverter
    fun fromStringList(list: List<String>): String {
        return json.toJson(list)
    }

    @TypeConverter
    fun toStringList(list: String): List<String> {
        return json.fromJson(list, Array<String>::class.java).toList()
    }

}