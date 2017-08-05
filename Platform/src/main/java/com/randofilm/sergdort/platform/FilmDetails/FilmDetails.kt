package com.randofilm.sergdort.platform.FilmDetails

import com.google.gson.annotations.SerializedName
import com.randofilm.sergdort.platform.Conversion.DomainConvertible
import com.randofilm.sergdort.domain.FilmDetails.FilmDetails
import java.util.*

internal data class FilmDetails(
        val id: Int,
        val title: String,
        val overview: String?,
        @SerializedName("vote_count")
        val voteCount: Int,
        @SerializedName("vote_average")
        val voteAverage: Double,
        @SerializedName("poster_path")
        val posterPath: String?,
        @SerializedName("backdrop_path")
        val backdropPath: String?,
        val budget: Int,
        val revenue: Int,
        @SerializedName("release_date")
        val releaseDate: Date) : DomainConvertible<FilmDetails> {

    override fun asDomain(): FilmDetails = FilmDetails(id, title, overview, voteCount, voteAverage, posterPath, backdropPath, budget, revenue, releaseDate)
}
