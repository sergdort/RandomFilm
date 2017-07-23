package com.randofilm.sergdort.domain.Film
import com.google.gson.annotations.SerializedName
import java.util.Date

data class Film(
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
        val releaseDate: Date) {}

