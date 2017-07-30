package com.randofilm.sergdort.platform.Film

import io.reactivex.Observable
import retrofit2.http.GET

internal interface FilmsAPI {
    @GET("3/discover/movie")
    fun discoverFilms(): Observable<FilmResults>
}
