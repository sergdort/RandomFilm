package com.randofilm.sergdort.domain.Film

import io.reactivex.Observable
import retrofit2.http.GET

interface FilmsAPI {
    @GET("3/discover/movie")
    fun discoverFilms(): Observable<FilmResults>;
}
