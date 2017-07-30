package com.randofilm.sergdort.domain.Film

data class FilmResults(
        val page: Int,
        val totalResults: Int,
        val totalPages: Int,
        val results: List<Film>) {}
