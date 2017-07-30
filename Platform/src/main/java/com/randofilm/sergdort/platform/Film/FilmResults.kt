package com.randofilm.sergdort.platform.Film
import com.google.gson.annotations.SerializedName
import com.randofilm.sergdort.domain.Film.FilmResults
import com.randofilm.sergdort.platform.Conversion.DomainConvertible

internal data class FilmResults(
        @SerializedName("page")
        val page: Int,
        @SerializedName("total_results")
        val totalResults: Int,
        @SerializedName("total_pages")
        val totalPages: Int,
        @SerializedName("results")
        val results: List<Film>): DomainConvertible<FilmResults> {

    override fun asDomain(): FilmResults {
        return FilmResults(page, totalResults, totalPages, results.map { it.asDomain() })
    }
}
