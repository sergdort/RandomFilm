package com.randofilm.sergdort.platform.Film
import com.google.gson.annotations.SerializedName
import com.randofilm.sergdort.domain.Film.Film
import com.randofilm.sergdort.platform.Conversion.DomainConvertible
import java.util.*

internal data class Film (
        @SerializedName("id")
        val id: Int,
        @SerializedName("title")
        val title: String,
        @SerializedName("poster_path")
        val posterPath: String,
        @SerializedName("backdrop_path")
        val backdropPath: String,
        @SerializedName("overview")
        val overview: String,
        @SerializedName("vote_average")
        val averageVote: Double,
        @SerializedName("release_date")
        val releaseDate: Date): DomainConvertible<Film> {

    override fun asDomain(): Film {
        return Film(id, title, posterPath, backdropPath, overview, averageVote, releaseDate)
    }
}
