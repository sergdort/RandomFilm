package com.randofilm.sergdort.platform.FilmDetails

import com.google.gson.annotations.SerializedName
import com.randofilm.sergdort.domain.FilmDetails.Cast
import com.randofilm.sergdort.platform.Conversion.DomainConvertible

internal data class Cast(
        @SerializedName("cast_id")
        val castID: Int,
        @SerializedName("character")
        val character: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("profile_path")
        val profilePath: String?) : DomainConvertible<Cast> {

    override fun asDomain(): Cast = Cast(castID, character, id, name, profilePath)
}
