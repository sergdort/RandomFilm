package com.randofilm.sergdort.domain.FilmDetails

data class FilmDetails(val id: Int,
                       val title: String,
                       val overview: String?,
                       val voteCount: Int,
                       val voteAverage: Double,
                       val posterPath: String?,
                       val backdropPath: String?,
                       val budget: Int,
                       val revenue: Int)
