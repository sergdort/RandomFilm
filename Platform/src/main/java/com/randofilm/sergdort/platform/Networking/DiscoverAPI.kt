package com.randofilm.sergdort.platform.Networking

import com.randofilm.sergdort.platform.Film.FilmResults
import io.reactivex.Observable
import retrofit2.http.*

internal interface DiscoverAPI {
    @GET("3/discover/movie")
    fun discoverFilms(): Observable<FilmResults>

    @GET("3/discover/movie")
    fun discoverFilms(@QueryMap queryMap: Map<String, String>): Observable<FilmResults>
}
