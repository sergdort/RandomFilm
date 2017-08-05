package com.randofilm.sergdort.domain.FilmDetails

import java.util.*

data class FilmDetails(val id: Int,
                       val title: String,
                       val overview: String?,
                       val voteCount: Int,
                       val voteAverage: Double,
                       val posterPath: String?,
                       val backdropPath: String?,
                       val budget: Int,
                       val revenue: Int,
                       val releaseDate: Date)
