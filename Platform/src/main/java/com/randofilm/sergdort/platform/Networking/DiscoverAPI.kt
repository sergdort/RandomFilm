package com.randofilm.sergdort.platform.Networking

import com.randofilm.sergdort.platform.Film.FilmResults
import io.reactivex.Observable
import retrofit2.http.GET

internal interface DiscoverAPI {
    @GET("3/discover/movie")
    fun discoverFilms(): Observable<FilmResults>
}
