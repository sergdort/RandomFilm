package com.randofilm.sergdort.domain.Film
import java.util.Date

data class Film(
        val id: Int,
        val title: String,
        val posterPath: String?,
        val backdropPath: String?,
        val overview: String,
        val averageVote: Double,
        val releaseDate: Date) {}

