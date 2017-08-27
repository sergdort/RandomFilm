package com.randofilm.sergdort.domain.Film

data class FilmResults(
        val page: Int,
        val totalResults: Int,
        val totalPages: Int,
        val results: List<Film>) {
    companion object {
        fun initial(): FilmResults {
            return FilmResults(1, 0, 0, listOf())
        }
    }

    fun resultsWithPosters(): List<Film> {
        return results.filter { it.posterPath != null }
    }
}
