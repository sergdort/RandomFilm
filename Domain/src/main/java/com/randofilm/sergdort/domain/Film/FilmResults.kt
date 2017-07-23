package com.randofilm.sergdort.domain.Film

import com.google.gson.annotations.SerializedName

data class FilmResults(
        @SerializedName("page")
        val page: Int,
        @SerializedName("total_results")
        val totalResults: Int,
        @SerializedName("total_pages")
        val totalPages: Int,
        @SerializedName("results")
        val results: Array<Film>) {}
